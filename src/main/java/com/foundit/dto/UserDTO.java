package com.foundit.dto;

import com.foundit.model.User;
public class UserDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String role;

    public UserDTO() {}

    public UserDTO(Long id, String fullName, String email, String phone) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    public static UserDTOBuilder builder() {
        return new UserDTOBuilder();
    }

    public static class UserDTOBuilder {
        private Long id;
        private String fullName;
        private String email;
        private String phone;
        public UserDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public UserDTOBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        public UserDTOBuilder email(String email) {
            this.email = email;
            return this;
        }
        public UserDTOBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }
        public UserDTO build() {
            return new UserDTO(this.id, this.fullName, this.email, this.phone);
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

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
