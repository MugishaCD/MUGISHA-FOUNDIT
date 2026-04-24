package com.foundit.repository;

import com.foundit.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByFoundItemId(Long foundItemId);
    boolean existsByLostItemIdAndFoundItemId(Long lostItemId, Long foundItemId);

    @Query("SELECT m FROM Match m WHERE m.lostItem.user.id = :userId OR m.foundItem.user.id = :userId")
    List<Match> findAllByUserId(@Param("userId") Long userId);
}
