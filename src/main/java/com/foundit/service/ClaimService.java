package com.foundit.service;

import com.foundit.dto.ClaimDTO;
import java.util.List;

public interface ClaimService {
    ClaimDTO create(Long userId, Long itemId);
    List<ClaimDTO> getAll();
    ClaimDTO getById(Long id);
    void delete(Long id);
    
    ClaimDTO approveClaim(Long id);
    ClaimDTO rejectClaim(Long id);
}
