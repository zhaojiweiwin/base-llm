package com.jiwei.base_llm.customer.model.dto;

import com.jiwei.base_llm.customer.model.entity.DocumentStatus;
import com.jiwei.base_llm.customer.model.entity.SourceType;

import java.time.Instant;

public record DocumentResponse(
		String id,
		String title,
		SourceType sourceType,
		String sourceUrl,
		DocumentStatus status,
		int chunkCount,
		Instant syncedAt
) {
}
