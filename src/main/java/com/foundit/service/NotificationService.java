package com.foundit.service;

import com.foundit.dto.NotificationDTO;
import com.foundit.model.Notification;
import java.util.List;

public interface NotificationService {
    NotificationDTO sendNotification(Long userId, String message);
    List<NotificationDTO> getUserNotifications(Long userId);
    List<NotificationDTO> getAll();
    NotificationDTO getById(Long id);
    NotificationDTO update(Long id, Notification notificationDetails);
    void delete(Long id);
}
