package com.jiwei.base_llm.common.model;

import java.util.List;

public record RagRequest(
		String query,
		List<String> history,
		String filterExpression
) {

	public RagRequest(String query) {
		this(query, List.of(), null);
	}

}
