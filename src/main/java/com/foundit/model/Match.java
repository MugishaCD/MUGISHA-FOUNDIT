package com.foundit.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double matchScore;

    private LocalDateTime matchDate;

    @PrePersist
    public void prePersist() {
        if (matchDate == null) {
            matchDate = LocalDateTime.now();
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lost_item_id", nullable = false)
    private LostItem lostItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "found_item_id", nullable = false)
    private FoundItem foundItem;

    public Match() {}

    public Match(Long id, Double matchScore, LocalDateTime matchDate, LostItem lostItem, FoundItem foundItem) {
        this.id = id;
        this.matchScore = matchScore;
        this.matchDate = matchDate;
        this.lostItem = lostItem;
        this.foundItem = foundItem;
    }

    public static MatchBuilder builder() {
        return new MatchBuilder();
    }

    public static class MatchBuilder {
        private Long id;
        private Double matchScore;
        private LocalDateTime matchDate;
        private LostItem lostItem;
        private FoundItem foundItem;
        public MatchBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public MatchBuilder matchScore(Double matchScore) {
            this.matchScore = matchScore;
            return this;
        }
        public MatchBuilder matchDate(LocalDateTime matchDate) {
            this.matchDate = matchDate;
            return this;
        }
        public MatchBuilder lostItem(LostItem lostItem) {
            this.lostItem = lostItem;
            return this;
        }
        public MatchBuilder foundItem(FoundItem foundItem) {
            this.foundItem = foundItem;
            return this;
        }
        public Match build() {
            return new Match(this.id, this.matchScore, this.matchDate, this.lostItem, this.foundItem);
        }
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMatchScore() {
        return this.matchScore;
    }

    public void setMatchScore(Double matchScore) {
        this.matchScore = matchScore;
    }

    public LocalDateTime getMatchDate() {
        return this.matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public LostItem getLostItem() {
        return this.lostItem;
    }

    public void setLostItem(LostItem lostItem) {
        this.lostItem = lostItem;
    }

    public FoundItem getFoundItem() {
        return this.foundItem;
    }

    public void setFoundItem(FoundItem foundItem) {
        this.foundItem = foundItem;
    }

}
