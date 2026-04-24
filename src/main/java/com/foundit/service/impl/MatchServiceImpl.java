package com.foundit.service.impl;

import com.foundit.dto.MatchDTO;
import com.foundit.dto.LostItemDTO;
import com.foundit.dto.FoundItemDTO;
import com.foundit.exception.ResourceNotFoundException;
import com.foundit.model.FoundItem;
import com.foundit.model.Item;
import com.foundit.model.LostItem;
import com.foundit.model.Match;
import com.foundit.repository.FoundItemRepository;
import com.foundit.repository.LostItemRepository;
import com.foundit.repository.MatchRepository;
import com.foundit.service.MatchService;
import com.foundit.service.NotificationService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final LostItemRepository lostItemRepository;
    private final FoundItemRepository foundItemRepository;
    private final NotificationService notificationService;
    public MatchServiceImpl(
            MatchRepository matchRepository, 
            LostItemRepository lostItemRepository, 
            FoundItemRepository foundItemRepository,
            NotificationService notificationService
    ) {
        this.matchRepository = matchRepository;
        this.lostItemRepository = lostItemRepository;
        this.foundItemRepository = foundItemRepository;
        this.notificationService = notificationService;
    }


    @Override
    public List<MatchDTO> getAll() {
        return matchRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MatchDTO getById(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id: " + id));
        return mapToDTO(match);
    }

    @Override
    public void delete(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id: " + id));
        matchRepository.delete(match);
    }

    @Override
    public void findMatches() {
        List<LostItem> pendingLostItems = lostItemRepository.findByStatus(LostItem.Status.PENDING);
        List<FoundItem> availableFoundItems = foundItemRepository.findByStatus(FoundItem.Status.AVAILABLE);

        for (LostItem lost : pendingLostItems) {
            matchItems(lost, availableFoundItems);
        }
    }

    @Override
    public void processMatchForLostItem(LostItem lostItem) {
        if (lostItem.getStatus() != LostItem.Status.PENDING) return;
        
        List<FoundItem> potentialMatches = foundItemRepository.findByStatusAndItemCategory(
                FoundItem.Status.AVAILABLE, 
                lostItem.getItem().getCategory()
        );
        matchItems(lostItem, potentialMatches);
    }

    @Override
    public void processMatchForFoundItem(FoundItem foundItem) {
        if (foundItem.getStatus() != FoundItem.Status.AVAILABLE) return;

        List<LostItem> potentialMatches = lostItemRepository.findByStatusAndItemCategory(
                LostItem.Status.PENDING, 
                foundItem.getItem().getCategory()
        );
        
        for (LostItem lost : potentialMatches) {
            matchSinglePair(lost, foundItem);
        }
    }

    private void matchItems(LostItem lost, List<FoundItem> foundItems) {
        for (FoundItem found : foundItems) {
            matchSinglePair(lost, found);
        }
    }

    private void matchSinglePair(LostItem lost, FoundItem found) {
        // Check if match already exists to avoid duplicates
        if (matchRepository.existsByLostItemIdAndFoundItemId(lost.getId(), found.getId())) {
            return;
        }

        double score = calculateSimilarity(lost, found);
        
        if (score >= 0.80) { // Lowered to 80% to ensure items catch more matches
            Match match = new Match();
            match.setLostItem(lost);
            match.setFoundItem(found);
            match.setMatchScore(score);
            match.setMatchDate(LocalDateTime.now());
            
            matchRepository.save(match);
            
            // Update statuses to MATCHED
            lost.setStatus(LostItem.Status.MATCHED);
            found.setStatus(FoundItem.Status.MATCHED);
            lostItemRepository.save(lost);
            foundItemRepository.save(found);

            // Notify the lost item owner with details
            String message = String.format(
                "A possible match for your lost item '%s' has been found.\n\n" +
                "Found Item Details:\n" +
                "- Name: %s\n" +
                "- Category: %s\n" +
                "- Color: %s\n" +
                "- Location: %s\n\n" +
                "Finder Contact Info:\n" +
                "- Contact Email: %s\n" +
                "- Contact Phone: %s",
                lost.getItem().getName(),
                found.getItem().getName(),
                found.getItem().getCategory(),
                found.getItem().getColor(),
                found.getLocationFound(),
                found.getUser().getEmail(),
                found.getUser().getPhone() != null ? found.getUser().getPhone() : "N/A"
            );
            
            notificationService.sendNotification(lost.getUser().getId(), message);
            
            // Also notify finder
            String finderMessage = String.format(
                "Great news! Your found item '%s' has been matched with a lost item report. " +
                "The owner has been notified and may contact you soon or submit a claim.",
                found.getItem().getName()
            );
            notificationService.sendNotification(found.getUser().getId(), finderMessage);
        }
    }

    private double calculateSimilarity(LostItem lost, FoundItem found) {
        Item lItem = lost.getItem();
        Item fItem = found.getItem();
        
        if (lItem == null || fItem == null) return 0.0;

        double score = 0.0;
        
        // 1. Mandatory Category Match (Fuzzy category check)
        String lCat = lItem.getCategory() != null ? lItem.getCategory().trim().toLowerCase() : "";
        String fCat = fItem.getCategory() != null ? fItem.getCategory().trim().toLowerCase() : "";
        
        if (lCat.isEmpty() || fCat.isEmpty()) return 0.0;
        
        // Match if same, or if one is a plural of the other (basic check)
        boolean catMatch = lCat.equals(fCat) || lCat.equals(fCat + "s") || fCat.equals(lCat + "s");
        
        if (!catMatch) {
            return 0.0; // Break early if categories are completely different
        }
        score += 25.0; // Base score for same category

        // 2. Name Similarity (Fuzzy) - 30 points
        if (lItem.getName() != null && fItem.getName() != null) {
            double nameSim = getStringSimilarity(lItem.getName().toLowerCase(), fItem.getName().toLowerCase());
            score += (nameSim * 30.0);
        }

        // 3. Color similarity - 20 points
        if (lItem.getColor() != null && fItem.getColor() != null) {
            if (lItem.getColor().trim().equalsIgnoreCase(fItem.getColor().trim())) {
                score += 20.0;
            } else if (lItem.getColor().toLowerCase().contains(fItem.getColor().toLowerCase()) || 
                       fItem.getColor().toLowerCase().contains(lItem.getColor().toLowerCase())) {
                score += 10.0;
            }
        }

        // 4. Location Proximity (Basic string check for now) - 25 points
        if (lost.getLocationLost() != null && found.getLocationFound() != null) {
            double locSim = getStringSimilarity(lost.getLocationLost().toLowerCase(), found.getLocationFound().toLowerCase());
            score += (locSim * 25.0);
        }

        return score / 100.0; 
    }

    private double getStringSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0.0;
        s1 = s1.trim();
        s2 = s2.trim();
        if (s1.isEmpty() || s2.isEmpty()) return 0.0;
        if (s1.equals(s2)) return 1.0;

        // Simple Jaccard similarity for words
        java.util.Set<String> set1 = java.util.Arrays.stream(s1.split("\\s+")).collect(java.util.stream.Collectors.toSet());
        java.util.Set<String> set2 = java.util.Arrays.stream(s2.split("\\s+")).collect(java.util.stream.Collectors.toSet());
        
        long intersection = set1.stream().filter(set2::contains).count();
        long union = java.util.stream.Stream.concat(set1.stream(), set2.stream()).distinct().count();
        
        return (double) intersection / union;
    }

    @Override
    public List<MatchDTO> getMatchesForUser(Long userId) {
        return matchRepository.findAllByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private MatchDTO mapToDTO(Match match) {
        MatchDTO dto = new MatchDTO();
        dto.setId(match.getId());
        dto.setMatchScore(match.getMatchScore());
        dto.setMatchDate(match.getMatchDate());
        
        if (match.getLostItem() != null) {
            LostItemDTO lDto = new LostItemDTO();
            lDto.setId(match.getLostItem().getId());
            lDto.setUserId(match.getLostItem().getUser().getId());
            
            if (match.getLostItem().getItem() != null) {
                com.foundit.dto.ItemDTO itemDto = new com.foundit.dto.ItemDTO();
                itemDto.setId(match.getLostItem().getItem().getId());
                itemDto.setName(match.getLostItem().getItem().getName());
                itemDto.setCategory(match.getLostItem().getItem().getCategory());
                itemDto.setColor(match.getLostItem().getItem().getColor());
                lDto.setItem(itemDto);
            }
            lDto.setLocationLost(match.getLostItem().getLocationLost());
            lDto.setStatus(match.getLostItem().getStatus().name());
            dto.setLostItem(lDto);
        }
        
        if (match.getFoundItem() != null) {
            FoundItemDTO fDto = new FoundItemDTO();
            fDto.setId(match.getFoundItem().getId());
            fDto.setUserId(match.getFoundItem().getUser().getId());

            if (match.getFoundItem().getItem() != null) {
                com.foundit.dto.ItemDTO itemDto = new com.foundit.dto.ItemDTO();
                itemDto.setId(match.getFoundItem().getItem().getId());
                itemDto.setName(match.getFoundItem().getItem().getName());
                itemDto.setCategory(match.getFoundItem().getItem().getCategory());
                itemDto.setColor(match.getFoundItem().getItem().getColor());
                fDto.setItem(itemDto);
            }
            fDto.setLocationFound(match.getFoundItem().getLocationFound());
            fDto.setStatus(match.getFoundItem().getStatus().name());
            dto.setFoundItem(fDto);
        }
        
        return dto;
    }
}
