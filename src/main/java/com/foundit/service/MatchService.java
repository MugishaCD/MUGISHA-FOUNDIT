package com.foundit.service;

import com.foundit.dto.MatchDTO;
import java.util.List;

public interface MatchService {
    List<MatchDTO> getAll();
    MatchDTO getById(Long id);
    void delete(Long id);
    
    void findMatches();
}
