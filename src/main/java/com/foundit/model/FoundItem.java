package com.foundit.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "found_items")
public class FoundItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateFound;
    private String locationFound;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public enum Status {
        AVAILABLE, MATCHED, RETURNED
    }

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = Status.AVAILABLE;
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    public FoundItem() {}

    public FoundItem(Long id, LocalDateTime dateFound, String locationFound, Status status, User user, Item item) {
        this.id = id;
        this.dateFound = dateFound;
        this.locationFound = locationFound;
        this.status = status;
        this.user = user;
        this.item = item;
    }

    public static FoundItemBuilder builder() {
        return new FoundItemBuilder();
    }

    public static class FoundItemBuilder {
        private Long id;
        private LocalDateTime dateFound;
        private String locationFound;
        private Status status;
        private User user;
        private Item item;
        public FoundItemBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public FoundItemBuilder dateFound(LocalDateTime dateFound) {
            this.dateFound = dateFound;
            return this;
        }
        public FoundItemBuilder locationFound(String locationFound) {
            this.locationFound = locationFound;
            return this;
        }
        public FoundItemBuilder status(Status status) {
            this.status = status;
            return this;
        }
        public FoundItemBuilder user(User user) {
            this.user = user;
            return this;
        }
        public FoundItemBuilder item(Item item) {
            this.item = item;
            return this;
        }
        public FoundItem build() {
            return new FoundItem(this.id, this.dateFound, this.locationFound, this.status, this.user, this.item);
        }
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateFound() {
        return this.dateFound;
    }

    public void setDateFound(LocalDateTime dateFound) {
        this.dateFound = dateFound;
    }

    public String getLocationFound() {
        return this.locationFound;
    }

    public void setLocationFound(String locationFound) {
        this.locationFound = locationFound;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

}
