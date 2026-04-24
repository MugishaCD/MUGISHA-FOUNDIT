package com.foundit.controller;

import com.foundit.dto.DashboardStatsDTO;
import com.foundit.repository.LostItemRepository;
import com.foundit.repository.FoundItemRepository;
import com.foundit.repository.MatchRepository;
import com.foundit.repository.ClaimRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final LostItemRepository lostItemRepository;
    private final FoundItemRepository foundItemRepository;
    private final MatchRepository matchRepository;
    private final ClaimRepository claimRepository;

    public StatsController(LostItemRepository lostItemRepository, FoundItemRepository foundItemRepository, 
                           MatchRepository matchRepository, ClaimRepository claimRepository) {
        this.lostItemRepository = lostItemRepository;
        this.foundItemRepository = foundItemRepository;
        this.matchRepository = matchRepository;
        this.claimRepository = claimRepository;
    }

    @GetMapping("/dashboard")
    public DashboardStatsDTO getDashboardStats() {
        return new DashboardStatsDTO(
            lostItemRepository.count(),
            foundItemRepository.count(),
            matchRepository.count(),
            claimRepository.count() // Assuming all claims represent active interest
        );
    }
}
