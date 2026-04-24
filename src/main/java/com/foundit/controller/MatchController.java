package com.foundit.controller;

import com.foundit.dto.MatchDTO;
import com.foundit.service.MatchService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }


    @PostMapping("/run")
    public ResponseEntity<Void> runMatchingLogic() {
        matchService.findMatches();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MatchDTO>> getAllMatches() {
        return ResponseEntity.ok(matchService.getAll());
    }

    @GetMapping("/my-matches")
    public ResponseEntity<List<MatchDTO>> getMyMatches(@AuthenticationPrincipal com.foundit.model.User user) {
        return ResponseEntity.ok(matchService.getMatchesForUser(user.getId()));
    }
}
