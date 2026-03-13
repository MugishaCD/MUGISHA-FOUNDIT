package com.foundit.dto;

import com.foundit.model.LostItem;

import java.time.LocalDateTime;
public class LostItemDTO {
    private Long id;
    private LocalDateTime dateLost;
    private String locationLost;
    private String status;
    private UserDTO user;
    private ItemDTO item;

    public LostItemDTO() {}

    public LostItemDTO(Long id, LocalDateTime dateLost, String locationLost, UserDTO user, ItemDTO item) {
        this.id = id;
        this.dateLost = dateLost;
        this.locationLost = locationLost;
        this.user = user;
        this.item = item;
    }

    public static LostItemDTOBuilder builder() {
        return new LostItemDTOBuilder();
    }

    public static class LostItemDTOBuilder {
        private Long id;
        private LocalDateTime dateLost;
        private String locationLost;
        private UserDTO user;
        private ItemDTO item;
        public LostItemDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public LostItemDTOBuilder dateLost(LocalDateTime dateLost) {
            this.dateLost = dateLost;
            return this;
        }
        public LostItemDTOBuilder locationLost(String locationLost) {
            this.locationLost = locationLost;
            return this;
        }
        public LostItemDTOBuilder user(UserDTO user) {
            this.user = user;
            return this;
        }
        public LostItemDTOBuilder item(ItemDTO item) {
            this.item = item;
            return this;
        }
        public LostItemDTO build() {
            return new LostItemDTO(this.id, this.dateLost, this.locationLost, this.user, this.item);
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
