package com.foundit.service.impl;

import com.foundit.dto.FoundItemDTO;
import com.foundit.dto.ItemDTO;
import com.foundit.dto.UserDTO;
import com.foundit.exception.ResourceNotFoundException;
import com.foundit.model.FoundItem;
import com.foundit.model.Item;
import com.foundit.model.User;
import com.foundit.repository.FoundItemRepository;
import com.foundit.repository.UserRepository;
import com.foundit.service.FoundItemService;
import com.foundit.service.MatchService;
import com.foundit.specification.FoundItemSpecification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoundItemServiceImpl implements FoundItemService {

    private final FoundItemRepository foundItemRepository;
    private final UserRepository userRepository;
    private final MatchService matchService;
 
    public FoundItemServiceImpl(FoundItemRepository foundItemRepository, UserRepository userRepository, MatchService matchService) {
        this.foundItemRepository = foundItemRepository;
        this.userRepository = userRepository;
        this.matchService = matchService;
    }


    @Override
    public FoundItemDTO create(Long userId, FoundItem foundItem) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        foundItem.setUser(user);
        FoundItem saved = foundItemRepository.save(foundItem);
        
        // Trigger automatic matching
        matchService.processMatchForFoundItem(saved);
        
        return mapToDTO(saved);
    }

    @Override
    public List<FoundItemDTO> getAll() {
        return foundItemRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FoundItemDTO getById(Long id) {
        FoundItem item = foundItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Found item not found with id: " + id));
        return mapToDTO(item);
    }

    @Override
    public FoundItemDTO update(Long id, FoundItem foundItemDetails) {
        FoundItem existing = foundItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Found item not found with id: " + id));
        
        existing.setDateFound(foundItemDetails.getDateFound());
        existing.setLocationFound(foundItemDetails.getLocationFound());
        if (foundItemDetails.getStatus() != null) {
            existing.setStatus(foundItemDetails.getStatus());
        }
        
        Item existingItem = existing.getItem();
        Item newItem = foundItemDetails.getItem();
        if (existingItem != null && newItem != null) {
            existingItem.setName(newItem.getName());
            existingItem.setCategory(newItem.getCategory());
            existingItem.setDescription(newItem.getDescription());
            existingItem.setColor(newItem.getColor());
        }

        FoundItem updated = foundItemRepository.save(existing);
        return mapToDTO(updated);
    }

    @Override
    public void delete(Long id) {
        FoundItem item = foundItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Found item not found with id: " + id));
        foundItemRepository.delete(item);
    }

    @Override
    public List<FoundItemDTO> search(String category, String location, String name, FoundItem.Status status) {
        Specification<FoundItem> spec = Specification.where(FoundItemSpecification.hasCategory(category))
                .and(FoundItemSpecification.hasLocation(location))
                .and(FoundItemSpecification.hasName(name))
                .and(FoundItemSpecification.hasStatus(status));
        
        return foundItemRepository.findAll(spec).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FoundItemDTO> getUserFoundItems(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return foundItemRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private FoundItemDTO mapToDTO(FoundItem foundItem) {
        FoundItemDTO dto = new FoundItemDTO();
        dto.setId(foundItem.getId());
        dto.setDateFound(foundItem.getDateFound());
        dto.setLocationFound(foundItem.getLocationFound());
        dto.setStatus(foundItem.getStatus().name());

        if (foundItem.getUser() != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(foundItem.getUser().getId());
            userDTO.setFullName(foundItem.getUser().getFullName());
            userDTO.setEmail(foundItem.getUser().getEmail());
            userDTO.setPhone(foundItem.getUser().getPhone());
            userDTO.setRole(foundItem.getUser().getRole().name());
            dto.setUser(userDTO);
        }

        if (foundItem.getItem() != null) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setId(foundItem.getItem().getId());
            itemDTO.setName(foundItem.getItem().getName());
            itemDTO.setCategory(foundItem.getItem().getCategory());
            itemDTO.setDescription(foundItem.getItem().getDescription());
            itemDTO.setColor(foundItem.getItem().getColor());
            dto.setItem(itemDTO);
        }
        return dto;
    }
}
