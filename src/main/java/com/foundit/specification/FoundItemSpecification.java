package com.foundit.specification;

import com.foundit.model.FoundItem;
import com.foundit.model.Item;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;

public class FoundItemSpecification {

    public static Specification<FoundItem> hasCategory(String category) {
        return (root, query, cb) -> {
            if (category == null || category.isEmpty()) return cb.conjunction();
            Join<FoundItem, Item> itemJoin = root.join("item");
            return cb.equal(itemJoin.get("category"), category);
        };
    }

    public static Specification<FoundItem> hasLocation(String location) {
        return (root, query, cb) -> {
            if (location == null || location.isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("locationFound")), "%" + location.toLowerCase() + "%");
        };
    }

    public static Specification<FoundItem> hasStatus(FoundItem.Status status) {
        return (root, query, cb) -> {
            if (status == null) return cb.conjunction();
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<FoundItem> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) return cb.conjunction();
            Join<FoundItem, Item> itemJoin = root.join("item");
            return cb.like(cb.lower(itemJoin.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
}
