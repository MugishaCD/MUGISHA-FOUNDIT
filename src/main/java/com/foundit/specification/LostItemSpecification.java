package com.foundit.specification;

import com.foundit.model.LostItem;
import com.foundit.model.Item;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;

public class LostItemSpecification {

    public static Specification<LostItem> hasCategory(String category) {
        return (root, query, cb) -> {
            if (category == null || category.isEmpty()) return cb.conjunction();
            Join<LostItem, Item> itemJoin = root.join("item");
            return cb.equal(itemJoin.get("category"), category);
        };
    }

    public static Specification<LostItem> hasLocation(String location) {
        return (root, query, cb) -> {
            if (location == null || location.isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("locationLost")), "%" + location.toLowerCase() + "%");
        };
    }

    public static Specification<LostItem> hasStatus(LostItem.Status status) {
        return (root, query, cb) -> {
            if (status == null) return cb.conjunction();
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<LostItem> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) return cb.conjunction();
            Join<LostItem, Item> itemJoin = root.join("item");
            return cb.like(cb.lower(itemJoin.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
}
