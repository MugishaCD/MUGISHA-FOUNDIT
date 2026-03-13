package com.foundit.dto;
public class ItemDTO {
    private Long id;
    private String name;
    private String category;
    private String description;
    private String color;

    public ItemDTO() {}

    public ItemDTO(Long id, String name, String category, String description, String color) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.color = color;
    }

    public static ItemDTOBuilder builder() {
        return new ItemDTOBuilder();
    }

    public static class ItemDTOBuilder {
        private Long id;
        private String name;
        private String category;
        private String description;
        private String color;
        public ItemDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public ItemDTOBuilder name(String name) {
            this.name = name;
            return this;
        }
        public ItemDTOBuilder category(String category) {
            this.category = category;
            return this;
        }
        public ItemDTOBuilder description(String description) {
            this.description = description;
            return this;
        }
        public ItemDTOBuilder color(String color) {
            this.color = color;
            return this;
        }
        public ItemDTO build() {
            return new ItemDTO(this.id, this.name, this.category, this.description, this.color);
        }
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
