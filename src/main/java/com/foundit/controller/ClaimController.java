package com.foundit.controller;

import com.foundit.dto.ClaimDTO;
import com.foundit.model.User;
import com.foundit.service.ClaimService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;
    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping
    public ResponseEntity<ClaimDTO> submitClaim(
            @AuthenticationPrincipal User user,
            @RequestParam Long itemId
    ) {
        return new ResponseEntity<>(claimService.create(user.getId(), itemId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ClaimDTO>> getAllClaims() {
        return ResponseEntity.ok(claimService.getAll());
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ClaimDTO> approveClaim(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.approveClaim(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ClaimDTO> rejectClaim(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.rejectClaim(id));
    }
}
