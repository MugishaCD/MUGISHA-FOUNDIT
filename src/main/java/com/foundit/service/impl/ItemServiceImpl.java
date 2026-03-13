package com.foundit.service.impl;

import com.foundit.dto.ItemDTO;
import com.foundit.exception.ResourceNotFoundException;
import com.foundit.model.Item;
import com.foundit.repository.ItemRepository;
import com.foundit.service.ItemService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    @Override
    public ItemDTO create(Item item) {
        Item savedItem = itemRepository.save(item);
        return mapToDTO(savedItem);
    }

    @Override
    public List<ItemDTO> getAll() {
        return itemRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDTO getById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        return mapToDTO(item);
    }

    @Override
    public ItemDTO update(Long id, Item itemDetails) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        
        item.setName(itemDetails.getName());
        item.setCategory(itemDetails.getCategory());
        item.setDescription(itemDetails.getDescription());
        item.setColor(itemDetails.getColor());

        Item updatedItem = itemRepository.save(item);
        return mapToDTO(updatedItem);
    }

    @Override
    public void delete(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        itemRepository.delete(item);
    }

    private ItemDTO mapToDTO(Item item) {
        if (item == null) return null;
        ItemDTO dto = new ItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setCategory(item.getCategory());
        dto.setDescription(item.getDescription());
        dto.setColor(item.getColor());
        return dto;
    }
}
