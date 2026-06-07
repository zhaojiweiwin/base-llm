package com.jiwei.base_llm.common.ai;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.EmbeddingModel;
import reactor.core.publisher.Flux;

public class LlmRouter {

	private final ChatModel chatModel;

	private final EmbeddingModel embeddingModel;

	public LlmRouter(ChatModel chatModel, EmbeddingModel embeddingModel) {
		this.chatModel = chatModel;
		this.embeddingModel = embeddingModel;
	}

	public ChatResponse chat(Prompt prompt) {
		return chatModel.call(prompt);
	}

	public Flux<ChatResponse> stream(Prompt prompt) {
		return chatModel.stream(prompt);
	}

	public float[] embed(String text) {
		return embeddingModel.embed(text);
	}

	public ChatModel chatModel() {
		return chatModel;
	}

	public EmbeddingModel embeddingModel() {
		return embeddingModel;
	}

}
