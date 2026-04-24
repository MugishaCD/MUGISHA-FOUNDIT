package com.foundit.controller;

import com.foundit.repository.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api/dev")
public class DevController {

    private final LostItemRepository lostItemRepository;
    private final FoundItemRepository foundItemRepository;
    private final MatchRepository matchRepository;
    private final ClaimRepository claimRepository;
    private final MessageRepository messageRepository;
    private final NotificationRepository notificationRepository;

    public DevController(LostItemRepository lostItemRepository,
                         FoundItemRepository foundItemRepository,
                         MatchRepository matchRepository,
                         ClaimRepository claimRepository,
                         MessageRepository messageRepository,
                         NotificationRepository notificationRepository) {
        this.lostItemRepository = lostItemRepository;
        this.foundItemRepository = foundItemRepository;
        this.matchRepository = matchRepository;
        this.claimRepository = claimRepository;
        this.messageRepository = messageRepository;
        this.notificationRepository = notificationRepository;
    }

    @PostMapping("/clear-items")
    @Transactional
    public String clearItems() {
        messageRepository.deleteAll();
        matchRepository.deleteAll();
        claimRepository.deleteAll();
        lostItemRepository.deleteAll();
        foundItemRepository.deleteAll();
        notificationRepository.deleteAll();
        return "All items, matches, claims, messages, and notifications have been cleared.";
    }
}
