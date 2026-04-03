package com.foundit.service;

import com.foundit.dto.LostItemDTO;
import com.foundit.model.LostItem;
import java.util.List;

public interface LostItemService {
    LostItemDTO create(Long userId, LostItem lostItem);
    List<LostItemDTO> getAll();
    LostItemDTO getById(Long id);
    LostItemDTO update(Long id, LostItem lostItemDetails);
    void delete(Long id);
    
    List<LostItemDTO> getUserLostItems(Long userId);
    List<LostItemDTO> search(String category, String location, String name, LostItem.Status status);
}
