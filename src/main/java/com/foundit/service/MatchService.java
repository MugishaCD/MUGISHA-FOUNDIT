package com.foundit.service;

import com.foundit.dto.MatchDTO;
import com.foundit.model.FoundItem;
import com.foundit.model.LostItem;
import java.util.List;

public interface MatchService {
    List<MatchDTO> getAll();
    MatchDTO getById(Long id);
    void delete(Long id);
    
    void findMatches();
    void processMatchForLostItem(LostItem lostItem);
    void processMatchForFoundItem(FoundItem foundItem);
    List<MatchDTO> getMatchesForUser(Long userId);
}
