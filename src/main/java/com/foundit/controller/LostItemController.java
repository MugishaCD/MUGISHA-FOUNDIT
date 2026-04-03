package com.foundit.controller;

import com.foundit.dto.LostItemDTO;
import com.foundit.model.LostItem;
import com.foundit.model.User;
import com.foundit.service.LostItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lost-items")
public class LostItemController {

    private final LostItemService lostItemService;
    public LostItemController(LostItemService lostItemService) {
        this.lostItemService = lostItemService;
    }

    @PostMapping
    public ResponseEntity<LostItemDTO> createLostItem(
            @AuthenticationPrincipal User user,
            @RequestBody LostItem lostItem
    ) {
        return new ResponseEntity<>(lostItemService.create(user.getId(), lostItem), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LostItemDTO>> getAllLostItems() {
        return ResponseEntity.ok(lostItemService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LostItemDTO> getLostItemById(@PathVariable Long id) {
        return ResponseEntity.ok(lostItemService.getById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LostItemDTO>> getUserLostItems(@PathVariable Long userId) {
        return ResponseEntity.ok(lostItemService.getUserLostItems(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<LostItemDTO>> searchLostItems(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) LostItem.Status status
    ) {
        return ResponseEntity.ok(lostItemService.search(category, location, name, status));
    }
}
