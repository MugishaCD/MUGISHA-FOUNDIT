package com.foundit.controller;

import com.foundit.dto.ItemDTO;
import com.foundit.model.Item;
import com.foundit.service.ItemService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@RequestBody Item item) {
        return new ResponseEntity<>(itemService.create(item), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        return ResponseEntity.ok(itemService.getAll());
    }
}
