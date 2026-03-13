package com.foundit.service.impl;

import com.foundit.dto.NotificationDTO;
import com.foundit.dto.UserDTO;
import com.foundit.exception.ResourceNotFoundException;
import com.foundit.model.Notification;
import com.foundit.model.User;
import com.foundit.repository.NotificationRepository;
import com.foundit.repository.UserRepository;
import com.foundit.service.NotificationService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }


    @Override
    public NotificationDTO sendNotification(Long userId, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setStatus(Notification.Status.UNREAD);
        notification.setCreatedAt(LocalDateTime.now());

        Notification saved = notificationRepository.save(notification);
        return mapToDTO(saved);
    }

    @Override
    public List<NotificationDTO> getUserNotifications(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return notificationRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getAll() {
        return notificationRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDTO getById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        return mapToDTO(notification);
    }

    @Override
    public NotificationDTO update(Long id, Notification notificationDetails) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        
        notification.setMessage(notificationDetails.getMessage());
        if (notificationDetails.getStatus() != null) {
            notification.setStatus(notificationDetails.getStatus());
        }

        Notification updated = notificationRepository.save(notification);
        return mapToDTO(updated);
    }

    @Override
    public void delete(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        notificationRepository.delete(notification);
    }

    private NotificationDTO mapToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setMessage(notification.getMessage());
        dto.setStatus(notification.getStatus().name());
        dto.setCreatedAt(notification.getCreatedAt());

        if (notification.getUser() != null) {
            UserDTO userDto = new UserDTO();
            userDto.setId(notification.getUser().getId());
            userDto.setFullName(notification.getUser().getFullName());
            dto.setUser(userDto);
        }

        return dto;
    }
}
