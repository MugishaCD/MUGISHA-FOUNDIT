package com.foundit.dto;

import java.time.LocalDateTime;
public class MatchDTO {
    private Long id;
    private Double matchScore;
    private LocalDateTime matchDate;
    private LostItemDTO lostItem;
    private FoundItemDTO foundItem;

    public MatchDTO() {}

    public MatchDTO(Long id, Double matchScore, LocalDateTime matchDate, LostItemDTO lostItem, FoundItemDTO foundItem) {
        this.id = id;
        this.matchScore = matchScore;
        this.matchDate = matchDate;
        this.lostItem = lostItem;
        this.foundItem = foundItem;
    }

    public static MatchDTOBuilder builder() {
        return new MatchDTOBuilder();
    }

    public static class MatchDTOBuilder {
        private Long id;
        private Double matchScore;
        private LocalDateTime matchDate;
        private LostItemDTO lostItem;
        private FoundItemDTO foundItem;
        public MatchDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public MatchDTOBuilder matchScore(Double matchScore) {
            this.matchScore = matchScore;
            return this;
        }
        public MatchDTOBuilder matchDate(LocalDateTime matchDate) {
            this.matchDate = matchDate;
            return this;
        }
        public MatchDTOBuilder lostItem(LostItemDTO lostItem) {
            this.lostItem = lostItem;
            return this;
        }
        public MatchDTOBuilder foundItem(FoundItemDTO foundItem) {
            this.foundItem = foundItem;
            return this;
        }
        public MatchDTO build() {
            return new MatchDTO(this.id, this.matchScore, this.matchDate, this.lostItem, this.foundItem);
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

    public LostItemDTO getLostItem() {
        return this.lostItem;
    }

    public void setLostItem(LostItemDTO lostItem) {
        this.lostItem = lostItem;
    }

    public FoundItemDTO getFoundItem() {
        return this.foundItem;
    }

    public void setFoundItem(FoundItemDTO foundItem) {
        this.foundItem = foundItem;
    }

}
