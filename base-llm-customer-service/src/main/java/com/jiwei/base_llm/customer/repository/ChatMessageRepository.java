package com.jiwei.base_llm.customer.repository;

import com.jiwei.base_llm.customer.model.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, String> {

	List<ChatMessageEntity> findBySessionIdOrderByCreatedAtAsc(String sessionId);

}
