package com.jiwei.base_llm.customer.model.dto;

import com.jiwei.base_llm.common.model.RetrievedChunk;

import java.util.List;

public record ChatCompletionResponse(
		String sessionId,
		String messageId,
		String answer,
		List<RetrievedChunk> citations,
		double confidence,
		boolean lowConfidence
) {
}
