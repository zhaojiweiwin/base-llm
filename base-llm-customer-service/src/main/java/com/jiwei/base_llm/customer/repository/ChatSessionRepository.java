package com.jiwei.base_llm.customer.repository;

import com.jiwei.base_llm.customer.model.entity.ChatSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSessionEntity, String> {
}
