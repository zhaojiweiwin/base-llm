package com.jiwei.base_llm.customer.config;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Flux;

import java.util.List;

@TestConfiguration
public class TestAiConfig {

	@Bean
	@Primary
	ChatModel chatModel() {
		return new ChatModel() {
			@Override
			public ChatResponse call(Prompt prompt) {
				return new ChatResponse(List.of(new Generation(new AssistantMessage("测试回答"))));
			}

			@Override
			public Flux<ChatResponse> stream(Prompt prompt) {
				return Flux.just(call(prompt));
			}
		};
	}

	@Bean
	@Primary
	EmbeddingModel embeddingModel() {
		return new EmbeddingModel() {
			@Override
			public EmbeddingResponse call(EmbeddingRequest request) {
				return new EmbeddingResponse(List.of(new Embedding(new float[] { 0.1f, 0.2f, 0.3f }, 0)));
			}

			@Override
			public float[] embed(String text) {
				return new float[] { 0.1f, 0.2f, 0.3f };
			}

			@Override
			public float[] embed(Document document) {
				return embed(document.getText());
			}
		};
	}

	@Bean
	@Primary
	VectorStore vectorStore(EmbeddingModel embeddingModel) {
		return SimpleVectorStore.builder(embeddingModel).build();
	}

}
