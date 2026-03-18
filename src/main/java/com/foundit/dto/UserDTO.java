package com.foundit.dto;

public class UserDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String securityPhotoUrl;

    public UserDTO() {}

    public UserDTO(Long id, String fullName, String email, String phone, String securityPhotoUrl) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.securityPhotoUrl = securityPhotoUrl;
    }

    public static UserDTOBuilder builder() {
        return new UserDTOBuilder();
    }

    public static class UserDTOBuilder {
        private Long id;
        private String fullName;
        private String email;
        private String phone;
        private String securityPhotoUrl;
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
        public UserDTOBuilder securityPhotoUrl(String securityPhotoUrl) {
            this.securityPhotoUrl = securityPhotoUrl;
            return this;
        }
        public UserDTO build() {
            return new UserDTO(this.id, this.fullName, this.email, this.phone, this.securityPhotoUrl);
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

    public String getSecurityPhotoUrl() {
        return this.securityPhotoUrl;
    }

    public void setSecurityPhotoUrl(String securityPhotoUrl) {
        this.securityPhotoUrl = securityPhotoUrl;
    }

}
