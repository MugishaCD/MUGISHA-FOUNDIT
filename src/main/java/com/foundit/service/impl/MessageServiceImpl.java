package com.foundit.service.impl;

import com.foundit.dto.MessageDTO;
import com.foundit.exception.ResourceNotFoundException;
import com.foundit.model.Match;
import com.foundit.model.Message;
import com.foundit.model.User;
import com.foundit.repository.MatchRepository;
import com.foundit.repository.MessageRepository;
import com.foundit.repository.UserRepository;
import com.foundit.service.MessageService;
import com.foundit.service.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public MessageServiceImpl(MessageRepository messageRepository, 
                              MatchRepository matchRepository, 
                              UserRepository userRepository,
                              NotificationService notificationService) {
        this.messageRepository = messageRepository;
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Override
    public MessageDTO sendMessage(Long matchId, Long senderId, String content) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Message message = new Message();
        message.setMatch(match);
        message.setSender(sender);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        Message saved = messageRepository.save(message);

        // Notify the other party
        User otherUser = match.getLostItem().getUser().getId().equals(senderId) 
                         ? match.getFoundItem().getUser() 
                         : match.getLostItem().getUser();
        
        notificationService.sendNotification(otherUser.getId(), 
            "New message from " + sender.getFullName() + " regarding item link: " + matchId);

        return mapToDTO(saved);
    }

    @Override
    public List<MessageDTO> getMessagesForMatch(Long matchId) {
        return messageRepository.findByMatchIdOrderByTimestampAsc(matchId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private MessageDTO mapToDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setMatchId(message.getMatch().getId());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderName(message.getSender().getFullName());
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());
        return dto;
    }
}
