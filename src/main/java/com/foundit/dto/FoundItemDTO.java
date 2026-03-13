package com.foundit.dto;

import com.foundit.model.FoundItem;

import java.time.LocalDateTime;
public class FoundItemDTO {
    private Long id;
    private LocalDateTime dateFound;
    private String locationFound;
    private String status;
    private UserDTO user;
    private ItemDTO item;

    public FoundItemDTO() {}

    public FoundItemDTO(Long id, LocalDateTime dateFound, String locationFound, UserDTO user, ItemDTO item) {
        this.id = id;
        this.dateFound = dateFound;
        this.locationFound = locationFound;
        this.user = user;
        this.item = item;
    }

    public static FoundItemDTOBuilder builder() {
        return new FoundItemDTOBuilder();
    }

    public static class FoundItemDTOBuilder {
        private Long id;
        private LocalDateTime dateFound;
        private String locationFound;
        private UserDTO user;
        private ItemDTO item;
        public FoundItemDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public FoundItemDTOBuilder dateFound(LocalDateTime dateFound) {
            this.dateFound = dateFound;
            return this;
        }
        public FoundItemDTOBuilder locationFound(String locationFound) {
            this.locationFound = locationFound;
            return this;
        }
        public FoundItemDTOBuilder user(UserDTO user) {
            this.user = user;
            return this;
        }
        public FoundItemDTOBuilder item(ItemDTO item) {
            this.item = item;
            return this;
        }
        public FoundItemDTO build() {
            return new FoundItemDTO(this.id, this.dateFound, this.locationFound, this.user, this.item);
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
