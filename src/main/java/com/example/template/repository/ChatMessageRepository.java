package com.example.template.repository;

import com.example.template.entity.ChatMessage;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
        List<ChatMessage> findAllByUserIdOrderByCreatedAtAsc(String userId);


    @Query("SELECT DATE(c.createdAt), COUNT(c) " +
            "FROM ChatMessage c " +
            "WHERE c.createdAt >= :startDate " +
            "GROUP BY DATE(c.createdAt)")
    List<Object[]> countMessagesByDate(@Param("startDate") LocalDateTime startDate);

    long countByRole(ChatMessage.MessageRole role);
}
