package com.foundit.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lost_items")
public class LostItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateLost;
    private String locationLost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public enum Status {
        PENDING, MATCHED, CLAIMED
    }

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = Status.PENDING;
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    public LostItem() {}

    public LostItem(Long id, LocalDateTime dateLost, String locationLost, Status status, User user, Item item) {
        this.id = id;
        this.dateLost = dateLost;
        this.locationLost = locationLost;
        this.status = status;
        this.user = user;
        this.item = item;
    }

    public static LostItemBuilder builder() {
        return new LostItemBuilder();
    }

    public static class LostItemBuilder {
        private Long id;
        private LocalDateTime dateLost;
        private String locationLost;
        private Status status;
        private User user;
        private Item item;
        public LostItemBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public LostItemBuilder dateLost(LocalDateTime dateLost) {
            this.dateLost = dateLost;
            return this;
        }
        public LostItemBuilder locationLost(String locationLost) {
            this.locationLost = locationLost;
            return this;
        }
        public LostItemBuilder status(Status status) {
            this.status = status;
            return this;
        }
        public LostItemBuilder user(User user) {
            this.user = user;
            return this;
        }
        public LostItemBuilder item(Item item) {
            this.item = item;
            return this;
        }
        public LostItem build() {
            return new LostItem(this.id, this.dateLost, this.locationLost, this.status, this.user, this.item);
        }
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateLost() {
        return this.dateLost;
    }

    public void setDateLost(LocalDateTime dateLost) {
        this.dateLost = dateLost;
    }

    public String getLocationLost() {
        return this.locationLost;
    }

    public void setLocationLost(String locationLost) {
        this.locationLost = locationLost;
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
