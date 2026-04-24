package com.foundit.repository;

import com.foundit.model.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LostItemRepository extends JpaRepository<LostItem, Long>, JpaSpecificationExecutor<LostItem> {
    List<LostItem> findByUserId(Long userId);
    List<LostItem> findByStatus(LostItem.Status status);
    List<LostItem> findByStatusAndItemCategory(LostItem.Status status, String category);
}
