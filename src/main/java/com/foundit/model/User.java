package com.foundit.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;
    
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime createdAt;
    
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public enum Role {
        USER, ADMIN
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LostItem> lostItems;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoundItem> foundItems;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Claim> claims;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    public User() {}

    public User(Long id, String fullName, String email, String phone, String password, Role role, LocalDateTime createdAt, List<LostItem> lostItems, List<FoundItem> foundItems, List<Claim> claims, List<Notification> notifications) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
        this.lostItems = lostItems;
        this.foundItems = foundItems;
        this.claims = claims;
        this.notifications = notifications;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private Long id;
        private String fullName;
        private String email;
        private String phone;
        private String password;
        private Role role;
        private LocalDateTime createdAt;
        private List<LostItem> lostItems;
        private List<FoundItem> foundItems;
        private List<Claim> claims;
        private List<Notification> notifications;
        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public UserBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }
        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }
        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }
        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }
        public UserBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public UserBuilder lostItems(List<LostItem> lostItems) {
            this.lostItems = lostItems;
            return this;
        }
        public UserBuilder foundItems(List<FoundItem> foundItems) {
            this.foundItems = foundItems;
            return this;
        }
        public UserBuilder claims(List<Claim> claims) {
            this.claims = claims;
            return this;
        }
        public UserBuilder notifications(List<Notification> notifications) {
            this.notifications = notifications;
            return this;
        }
        public User build() {
            return new User(this.id, this.fullName, this.email, this.phone, this.password, this.role, this.createdAt, this.lostItems, this.foundItems, this.claims, this.notifications);
        }
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<LostItem> getLostItems() {
        return this.lostItems;
    }

    public void setLostItems(List<LostItem> lostItems) {
        this.lostItems = lostItems;
    }

    public List<FoundItem> getFoundItems() {
        return this.foundItems;
    }

    public void setFoundItems(List<FoundItem> foundItems) {
        this.foundItems = foundItems;
    }

    public List<Claim> getClaims() {
        return this.claims;
    }

    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }

    public List<Notification> getNotifications() {
        return this.notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

}
