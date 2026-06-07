package com.jiwei.base_llm.customer.repository;

import com.jiwei.base_llm.customer.model.entity.KbDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KbDocumentRepository extends JpaRepository<KbDocumentEntity, String> {
}
