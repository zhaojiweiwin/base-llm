package com.jiwei.base_llm.customer.repository;

import com.jiwei.base_llm.customer.model.entity.ChatFeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatFeedbackRepository extends JpaRepository<ChatFeedbackEntity, String> {
}
