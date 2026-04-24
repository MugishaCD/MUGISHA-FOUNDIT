package com.foundit.repository;

import com.foundit.model.FoundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoundItemRepository extends JpaRepository<FoundItem, Long>, JpaSpecificationExecutor<FoundItem> {
    List<FoundItem> findByUserId(Long userId);
    Optional<FoundItem> findByItemId(Long itemId);
    List<FoundItem> findByStatus(FoundItem.Status status);
    List<FoundItem> findByStatusAndItemCategory(FoundItem.Status status, String category);
}
