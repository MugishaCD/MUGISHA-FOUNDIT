package com.foundit.service.impl;

import com.foundit.dto.ItemDTO;
import com.foundit.dto.LostItemDTO;
import com.foundit.dto.UserDTO;
import com.foundit.exception.ResourceNotFoundException;
import com.foundit.model.Item;
import com.foundit.model.LostItem;
import com.foundit.model.User;
import com.foundit.repository.LostItemRepository;
import com.foundit.repository.UserRepository;
import com.foundit.service.LostItemService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LostItemServiceImpl implements LostItemService {

    private final LostItemRepository lostItemRepository;
    private final UserRepository userRepository;
    public LostItemServiceImpl(LostItemRepository lostItemRepository, UserRepository userRepository) {
        this.lostItemRepository = lostItemRepository;
        this.userRepository = userRepository;
    }


    @Override
    public LostItemDTO create(Long userId, LostItem lostItem) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        lostItem.setUser(user);
        LostItem saved = lostItemRepository.save(lostItem);
        return mapToDTO(saved);
    }

    @Override
    public List<LostItemDTO> getAll() {
        return lostItemRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LostItemDTO getById(Long id) {
        LostItem item = lostItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lost item not found with id: " + id));
        return mapToDTO(item);
    }

    @Override
    public LostItemDTO update(Long id, LostItem lostItemDetails) {
        LostItem existing = lostItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lost item not found with id: " + id));
        
        existing.setDateLost(lostItemDetails.getDateLost());
        existing.setLocationLost(lostItemDetails.getLocationLost());
        if (lostItemDetails.getStatus() != null) {
            existing.setStatus(lostItemDetails.getStatus());
        }
        
        Item existingItem = existing.getItem();
        Item newItem = lostItemDetails.getItem();
        if (existingItem != null && newItem != null) {
            existingItem.setName(newItem.getName());
            existingItem.setCategory(newItem.getCategory());
            existingItem.setDescription(newItem.getDescription());
            existingItem.setColor(newItem.getColor());
        }

        LostItem updated = lostItemRepository.save(existing);
        return mapToDTO(updated);
    }

    @Override
    public void delete(Long id) {
        LostItem item = lostItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lost item not found with id: " + id));
        lostItemRepository.delete(item);
    }

    @Override
    public List<LostItemDTO> getUserLostItems(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return lostItemRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private LostItemDTO mapToDTO(LostItem lostItem) {
        LostItemDTO dto = new LostItemDTO();
        dto.setId(lostItem.getId());
        dto.setDateLost(lostItem.getDateLost());
        dto.setLocationLost(lostItem.getLocationLost());
        dto.setStatus(lostItem.getStatus().name());

        if (lostItem.getUser() != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(lostItem.getUser().getId());
            userDTO.setFullName(lostItem.getUser().getFullName());
            userDTO.setEmail(lostItem.getUser().getEmail());
            userDTO.setPhone(lostItem.getUser().getPhone());
            userDTO.setRole(lostItem.getUser().getRole().name());
            dto.setUser(userDTO);
        }

        if (lostItem.getItem() != null) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setId(lostItem.getItem().getId());
            itemDTO.setName(lostItem.getItem().getName());
            itemDTO.setCategory(lostItem.getItem().getCategory());
            itemDTO.setDescription(lostItem.getItem().getDescription());
            itemDTO.setColor(lostItem.getItem().getColor());
            dto.setItem(itemDTO);
        }
        return dto;
    }
}
