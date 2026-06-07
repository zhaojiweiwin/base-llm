package com.jiwei.base_llm.common.vector;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;
import java.util.Map;

public class VectorStoreService {

	private final VectorStore vectorStore;

	public VectorStoreService(VectorStore vectorStore) {
		this.vectorStore = vectorStore;
	}

	public void addDocuments(List<Document> documents) {
		vectorStore.add(documents);
	}

	public List<Document> similaritySearch(String query, int topK, String filterExpression) {
		SearchRequest.Builder builder = SearchRequest.builder().query(query).topK(topK);
		if (filterExpression != null && !filterExpression.isBlank()) {
			builder.filterExpression(filterExpression);
		}
		return vectorStore.similaritySearch(builder.build());
	}

	public void deleteByFilter(String filterExpression) {
		vectorStore.delete(filterExpression);
	}

	public static Document toDocument(String content, Map<String, Object> metadata) {
		return new Document(content, metadata);
	}

}
