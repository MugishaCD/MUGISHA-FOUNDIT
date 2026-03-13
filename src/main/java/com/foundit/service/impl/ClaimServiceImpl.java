package com.foundit.service.impl;

import com.foundit.dto.ClaimDTO;
import com.foundit.dto.ItemDTO;
import com.foundit.dto.UserDTO;
import com.foundit.exception.ResourceNotFoundException;
import com.foundit.model.Claim;
import com.foundit.model.Item;
import com.foundit.model.User;
import com.foundit.repository.ClaimRepository;
import com.foundit.repository.ItemRepository;
import com.foundit.repository.UserRepository;
import com.foundit.service.ClaimService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    public ClaimServiceImpl(ClaimRepository claimRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.claimRepository = claimRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }


    @Override
    public ClaimDTO create(Long userId, Long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + itemId));

        Claim claim = new Claim();
        claim.setUser(user);
        claim.setItem(item);
        claim.setStatus(Claim.Status.PENDING);
        claim.setClaimDate(LocalDateTime.now());

        Claim saved = claimRepository.save(claim);
        return mapToDTO(saved);
    }

    @Override
    public List<ClaimDTO> getAll() {
        return claimRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClaimDTO getById(Long id) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + id));
        return mapToDTO(claim);
    }

    @Override
    public void delete(Long id) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + id));
        claimRepository.delete(claim);
    }

    @Override
    public ClaimDTO approveClaim(Long id) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + id));
        
        claim.setStatus(Claim.Status.APPROVED);
        Claim updated = claimRepository.save(claim);
        return mapToDTO(updated);
    }

    @Override
    public ClaimDTO rejectClaim(Long id) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + id));
        
        claim.setStatus(Claim.Status.REJECTED);
        Claim updated = claimRepository.save(claim);
        return mapToDTO(updated);
    }

    private ClaimDTO mapToDTO(Claim claim) {
        ClaimDTO dto = new ClaimDTO();
        dto.setId(claim.getId());
        dto.setClaimDate(claim.getClaimDate());
        dto.setStatus(claim.getStatus().name());

        if (claim.getUser() != null) {
            UserDTO userDto = new UserDTO();
            userDto.setId(claim.getUser().getId());
            userDto.setFullName(claim.getUser().getFullName());
            dto.setUser(userDto);
        }

        if (claim.getItem() != null) {
            ItemDTO itemDto = new ItemDTO();
            itemDto.setId(claim.getItem().getId());
            itemDto.setName(claim.getItem().getName());
            dto.setItem(itemDto);
        }

        return dto;
    }
}
