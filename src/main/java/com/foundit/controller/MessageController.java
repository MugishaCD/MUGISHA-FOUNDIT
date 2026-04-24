package com.foundit.controller;

import com.foundit.dto.MessageDTO;
import com.foundit.model.User;
import com.foundit.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/{matchId}")
    public ResponseEntity<MessageDTO> sendMessage(
            @PathVariable Long matchId,
            @AuthenticationPrincipal User user,
            @RequestBody String content
    ) {
        // Handle raw string content or simple request body
        return ResponseEntity.ok(messageService.sendMessage(matchId, user.getId(), content));
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<List<MessageDTO>> getMessages(
            @PathVariable Long matchId
    ) {
        return ResponseEntity.ok(messageService.getMessagesForMatch(matchId));
    }
}
