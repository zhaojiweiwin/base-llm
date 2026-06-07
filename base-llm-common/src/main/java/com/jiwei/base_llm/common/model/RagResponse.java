package com.jiwei.base_llm.common.model;

import java.util.List;

public record RagResponse(
		String answer,
		List<RetrievedChunk> citations,
		double confidence,
		boolean lowConfidence
) {
}
