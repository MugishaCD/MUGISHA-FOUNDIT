package com.foundit.repository;

import com.foundit.model.FoundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoundItemRepository extends JpaRepository<FoundItem, Long> {
    List<FoundItem> findByUserId(Long userId);
}
