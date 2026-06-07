package com.jiwei.base_llm.common.model;

import java.util.Map;

public record RetrievedChunk(
		String content,
		double score,
		String documentId,
		String documentTitle,
		String sourceUrl,
		String sectionPath,
		Map<String, Object> metadata
) {
}
