package com.foundit.service;

import com.foundit.dto.FoundItemDTO;
import com.foundit.model.FoundItem;
import java.util.List;

public interface FoundItemService {
    FoundItemDTO create(Long userId, FoundItem foundItem);
    List<FoundItemDTO> getAll();
    FoundItemDTO getById(Long id);
    FoundItemDTO update(Long id, FoundItem foundItemDetails);
    void delete(Long id);
    
    List<FoundItemDTO> getUserFoundItems(Long userId);
}
