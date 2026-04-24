package com.foundit.service;

import com.foundit.dto.MessageDTO;
import java.util.List;

public interface MessageService {
    MessageDTO sendMessage(Long matchId, Long senderId, String content);
    List<MessageDTO> getMessagesForMatch(Long matchId);
}
