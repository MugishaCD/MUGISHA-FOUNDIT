package com.foundit.dto;

import com.foundit.model.Claim;

import java.time.LocalDateTime;
public class ClaimDTO {
    private Long id;
    private LocalDateTime claimDate;
    private String status;
    private UserDTO user;
    private ItemDTO item;

    public ClaimDTO() {}

    public ClaimDTO(Long id, LocalDateTime claimDate, UserDTO user, ItemDTO item) {
        this.id = id;
        this.claimDate = claimDate;
        this.user = user;
        this.item = item;
    }

    public static ClaimDTOBuilder builder() {
        return new ClaimDTOBuilder();
    }

    public static class ClaimDTOBuilder {
        private Long id;
        private LocalDateTime claimDate;
        private UserDTO user;
        private ItemDTO item;
        public ClaimDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public ClaimDTOBuilder claimDate(LocalDateTime claimDate) {
            this.claimDate = claimDate;
            return this;
        }
        public ClaimDTOBuilder user(UserDTO user) {
            this.user = user;
            return this;
        }
        public ClaimDTOBuilder item(ItemDTO item) {
            this.item = item;
            return this;
        }
        public ClaimDTO build() {
            return new ClaimDTO(this.id, this.claimDate, this.user, this.item);
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

    public UserDTO getUser() {
        return this.user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public ItemDTO getItem() {
        return this.item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
