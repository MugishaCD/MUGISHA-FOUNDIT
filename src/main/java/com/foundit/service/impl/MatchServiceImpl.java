package com.foundit.service.impl;

import com.foundit.dto.MatchDTO;
import com.foundit.dto.LostItemDTO;
import com.foundit.dto.FoundItemDTO;
import com.foundit.dto.ItemDTO;
import com.foundit.dto.UserDTO;
import com.foundit.exception.ResourceNotFoundException;
import com.foundit.model.FoundItem;
import com.foundit.model.Item;
import com.foundit.model.LostItem;
import com.foundit.model.Match;
import com.foundit.repository.FoundItemRepository;
import com.foundit.repository.LostItemRepository;
import com.foundit.repository.MatchRepository;
import com.foundit.service.MatchService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final LostItemRepository lostItemRepository;
    private final FoundItemRepository foundItemRepository;
    public MatchServiceImpl(MatchRepository matchRepository, LostItemRepository lostItemRepository, FoundItemRepository foundItemRepository) {
        this.matchRepository = matchRepository;
        this.lostItemRepository = lostItemRepository;
        this.foundItemRepository = foundItemRepository;
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
        List<LostItem> pendingLostItems = lostItemRepository.findAll().stream()
                .filter(item -> item.getStatus() == LostItem.Status.PENDING)
                .collect(Collectors.toList());
                
        List<FoundItem> availableFoundItems = foundItemRepository.findAll().stream()
                .filter(item -> item.getStatus() == FoundItem.Status.AVAILABLE)
                .collect(Collectors.toList());

        for (LostItem lost : pendingLostItems) {
            for (FoundItem found : availableFoundItems) {
                double score = calculateSimilarity(lost, found);
                if (score > 0.6) { // Threshold for a match
                    Match match = new Match();
                    match.setLostItem(lost);
                    match.setFoundItem(found);
                    match.setMatchScore(score);
                    match.setMatchDate(LocalDateTime.now());
                    
                    matchRepository.save(match);
                    
                    // Optionally update status to MATCHED
                    lost.setStatus(LostItem.Status.MATCHED);
                    found.setStatus(FoundItem.Status.MATCHED);
                    lostItemRepository.save(lost);
                    foundItemRepository.save(found);
                }
            }
        }
    }

    private double calculateSimilarity(LostItem lost, FoundItem found) {
        Item lItem = lost.getItem();
        Item fItem = found.getItem();
        
        if (lItem == null || fItem == null) return 0.0;

        double score = 0.0;
        int totalCriteria = 4;

        if (lItem.getName() != null && lItem.getName().equalsIgnoreCase(fItem.getName())) score += 1.0;
        if (lItem.getCategory() != null && lItem.getCategory().equalsIgnoreCase(fItem.getCategory())) score += 1.0;
        if (lItem.getColor() != null && lItem.getColor().equalsIgnoreCase(fItem.getColor())) score += 1.0;
        if (lost.getLocationLost() != null && found.getLocationFound() != null && 
            lost.getLocationLost().toLowerCase().contains(found.getLocationFound().toLowerCase())) {
            score += 1.0;
        }

        return score / totalCriteria;
    }

    private MatchDTO mapToDTO(Match match) {
        MatchDTO dto = new MatchDTO();
        dto.setId(match.getId());
        dto.setMatchScore(match.getMatchScore());
        dto.setMatchDate(match.getMatchDate());
        
        // Simplified mapping for the DTO
        if (match.getLostItem() != null) {
            LostItemDTO lDto = new LostItemDTO();
            lDto.setId(match.getLostItem().getId());
            lDto.setLocationLost(match.getLostItem().getLocationLost());
            dto.setLostItem(lDto);
        }
        
        if (match.getFoundItem() != null) {
            FoundItemDTO fDto = new FoundItemDTO();
            fDto.setId(match.getFoundItem().getId());
            fDto.setLocationFound(match.getFoundItem().getLocationFound());
            dto.setFoundItem(fDto);
        }
        
        return dto;
    }
}
