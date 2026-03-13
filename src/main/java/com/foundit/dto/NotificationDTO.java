package com.foundit.dto;

import com.foundit.model.Notification;

import java.time.LocalDateTime;
public class NotificationDTO {
    private Long id;
    private String message;
    private String status;
    private LocalDateTime createdAt;
    private UserDTO user;

    public NotificationDTO() {}

    public NotificationDTO(Long id, String message, LocalDateTime createdAt, UserDTO user) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.user = user;
    }

    public static NotificationDTOBuilder builder() {
        return new NotificationDTOBuilder();
    }

    public static class NotificationDTOBuilder {
        private Long id;
        private String message;
        private LocalDateTime createdAt;
        private UserDTO user;
        public NotificationDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public NotificationDTOBuilder message(String message) {
            this.message = message;
            return this;
        }
        public NotificationDTOBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public NotificationDTOBuilder user(UserDTO user) {
            this.user = user;
            return this;
        }
        public NotificationDTO build() {
            return new NotificationDTO(this.id, this.message, this.createdAt, this.user);
        }
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserDTO getUser() {
        return this.user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
