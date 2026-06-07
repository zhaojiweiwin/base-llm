package com.jiwei.base_llm.common.model;

import java.util.Map;

public record DocumentChunk(
		String content,
		int index,
		String sectionPath,
		Map<String, Object> metadata
) {
}
