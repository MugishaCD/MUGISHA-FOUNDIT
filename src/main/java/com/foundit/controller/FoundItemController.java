package com.foundit.controller;

import com.foundit.dto.FoundItemDTO;
import com.foundit.model.FoundItem;
import com.foundit.model.User;
import com.foundit.service.FoundItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/found-items")
public class FoundItemController {

    private final FoundItemService foundItemService;
    public FoundItemController(FoundItemService foundItemService) {
        this.foundItemService = foundItemService;
    }

    @PostMapping
    public ResponseEntity<FoundItemDTO> createFoundItem(
            @AuthenticationPrincipal User user,
            @RequestBody FoundItem foundItem
    ) {
        return new ResponseEntity<>(foundItemService.create(user.getId(), foundItem), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FoundItemDTO>> getAllFoundItems() {
        return ResponseEntity.ok(foundItemService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoundItemDTO> getFoundItemById(@PathVariable Long id) {
        return ResponseEntity.ok(foundItemService.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<FoundItemDTO>> searchFoundItems(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) FoundItem.Status status
    ) {
        return ResponseEntity.ok(foundItemService.search(category, location, name, status));
    }
}
