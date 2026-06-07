package com.jiwei.base_llm.common.rag;

import com.jiwei.base_llm.common.ai.LlmRouter;
import com.jiwei.base_llm.common.model.RagRequest;
import com.jiwei.base_llm.common.model.RagResponse;
import com.jiwei.base_llm.common.model.RetrievedChunk;
import com.jiwei.base_llm.common.prompt.PromptTemplateEngine;
import com.jiwei.base_llm.common.vector.VectorStoreService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

public class RagPipeline {

	private final LlmRouter llmRouter;

	private final VectorStoreService vectorStoreService;

	private final PromptTemplateEngine promptTemplateEngine;

	private final int topK;

	private final double similarityThreshold;

	public RagPipeline(LlmRouter llmRouter, VectorStoreService vectorStoreService,
			PromptTemplateEngine promptTemplateEngine, int topK, double similarityThreshold) {
		this.llmRouter = llmRouter;
		this.vectorStoreService = vectorStoreService;
		this.promptTemplateEngine = promptTemplateEngine;
		this.topK = topK;
		this.similarityThreshold = similarityThreshold;
	}

	public RagResponse query(RagRequest request) {
		List<RetrievedChunk> citations = retrieve(request.query(), request.filterExpression());
		double confidence = citations.isEmpty() ? 0.0 : citations.getFirst().score();
		boolean lowConfidence = confidence < similarityThreshold;

		Prompt prompt = promptTemplateEngine.buildPrompt(request.query(), citations, request.history());
		ChatResponse response = llmRouter.chat(prompt);
		String answer = response.getResult().getOutput().getText();

		if (lowConfidence) {
			answer = answer + "\n\n（提示：检索置信度较低，建议转人工进一步确认。）";
		}

		return new RagResponse(answer, citations, confidence, lowConfidence);
	}

	public Flux<String> stream(RagRequest request) {
		List<RetrievedChunk> citations = retrieve(request.query(), request.filterExpression());
		Prompt prompt = promptTemplateEngine.buildPrompt(request.query(), citations, request.history());
		return llmRouter.stream(prompt)
				.map(chatResponse -> chatResponse.getResult().getOutput().getText())
				.filter(token -> token != null && !token.isBlank());
	}

	public List<RetrievedChunk> retrieve(String query, String filterExpression) {
		List<Document> documents = vectorStoreService.similaritySearch(query, topK, filterExpression);
		return documents.stream().map(this::toRetrievedChunk).toList();
	}

	private RetrievedChunk toRetrievedChunk(Document document) {
		Map<String, Object> metadata = document.getMetadata();
		double score = metadata.get("distance") instanceof Number number ? 1.0 - number.doubleValue()
				: metadata.get("score") instanceof Number scoreNumber ? scoreNumber.doubleValue() : 0.5;

		return new RetrievedChunk(
				document.getText(),
				score,
				stringValue(metadata.get("documentId")),
				stringValue(metadata.get("documentTitle")),
				stringValue(metadata.get("sourceUrl")),
				stringValue(metadata.get("sectionPath")),
				metadata);
	}

	private String stringValue(Object value) {
		return value == null ? null : value.toString();
	}

}
