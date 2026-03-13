package com.foundit.service;

import com.foundit.dto.ItemDTO;
import com.foundit.model.Item;
import java.util.List;

public interface ItemService {
    ItemDTO create(Item item);
    List<ItemDTO> getAll();
    ItemDTO getById(Long id);
    ItemDTO update(Long id, Item itemDetails);
    void delete(Long id);
}
