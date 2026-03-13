package com.foundit.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public enum Status {
        READ, UNREAD
    }

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = Status.UNREAD;
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Notification() {}

    public Notification(Long id, String message, Status status, LocalDateTime createdAt, User user) {
        this.id = id;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
        this.user = user;
    }

    public static NotificationBuilder builder() {
        return new NotificationBuilder();
    }

    public static class NotificationBuilder {
        private Long id;
        private String message;
        private Status status;
        private LocalDateTime createdAt;
        private User user;
        public NotificationBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public NotificationBuilder message(String message) {
            this.message = message;
            return this;
        }
        public NotificationBuilder status(Status status) {
            this.status = status;
            return this;
        }
        public NotificationBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public NotificationBuilder user(User user) {
            this.user = user;
            return this;
        }
        public Notification build() {
            return new Notification(this.id, this.message, this.status, this.createdAt, this.user);
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

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
