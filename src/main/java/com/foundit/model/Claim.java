package com.foundit.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "claims")
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime claimDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public enum Status {
        PENDING, APPROVED, REJECTED
    }

    @PrePersist
    public void prePersist() {
        if (claimDate == null) {
            claimDate = LocalDateTime.now();
        }
        if (status == null) {
            status = Status.PENDING;
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    public Claim() {}

    public Claim(Long id, LocalDateTime claimDate, Status status, User user, Item item) {
        this.id = id;
        this.claimDate = claimDate;
        this.status = status;
        this.user = user;
        this.item = item;
    }

    public static ClaimBuilder builder() {
        return new ClaimBuilder();
    }

    public static class ClaimBuilder {
        private Long id;
        private LocalDateTime claimDate;
        private Status status;
        private User user;
        private Item item;
        public ClaimBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public ClaimBuilder claimDate(LocalDateTime claimDate) {
            this.claimDate = claimDate;
            return this;
        }
        public ClaimBuilder status(Status status) {
            this.status = status;
            return this;
        }
        public ClaimBuilder user(User user) {
            this.user = user;
            return this;
        }
        public ClaimBuilder item(Item item) {
            this.item = item;
            return this;
        }
        public Claim build() {
            return new Claim(this.id, this.claimDate, this.status, this.user, this.item);
        }
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getClaimDate() {
        return this.claimDate;
    }

    public void setClaimDate(LocalDateTime claimDate) {
        this.claimDate = claimDate;
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
