package com.foundit.model;

import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(length = 1000)
    private String description;

    private String color;

    public Item() {}

    public Item(Long id, String name, String category, String description, String color) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.color = color;
    }

    public static ItemBuilder builder() {
        return new ItemBuilder();
    }

    public static class ItemBuilder {
        private Long id;
        private String name;
        private String category;
        private String description;
        private String color;
        public ItemBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public ItemBuilder name(String name) {
            this.name = name;
            return this;
        }
        public ItemBuilder category(String category) {
            this.category = category;
            return this;
        }
        public ItemBuilder description(String description) {
            this.description = description;
            return this;
        }
        public ItemBuilder color(String color) {
            this.color = color;
            return this;
        }
        public Item build() {
            return new Item(this.id, this.name, this.category, this.description, this.color);
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
