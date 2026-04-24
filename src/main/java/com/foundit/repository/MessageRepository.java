package com.foundit.repository;

import com.foundit.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByMatchIdOrderByTimestampAsc(Long matchId);
}
