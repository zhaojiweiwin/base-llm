package com.jiwei.base_llm.common.config;

import com.jiwei.base_llm.common.ai.LlmRouter;
import com.jiwei.base_llm.common.parser.DocumentChunker;
import com.jiwei.base_llm.common.parser.DocumentParser;
import com.jiwei.base_llm.common.prompt.PromptTemplateEngine;
import com.jiwei.base_llm.common.rag.RagPipeline;
import com.jiwei.base_llm.common.vector.VectorStoreService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(LlmProperties.class)
public class BaseLlmCommonAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	LlmRouter llmRouter(ChatModel chatModel, EmbeddingModel embeddingModel) {
		return new LlmRouter(chatModel, embeddingModel);
	}

	@Bean
	@ConditionalOnMissingBean
	DocumentParser documentParser() {
		return new DocumentParser();
	}

	@Bean
	@ConditionalOnMissingBean
	DocumentChunker documentChunker(LlmProperties properties) {
		return new DocumentChunker(properties.getRag().getChunkSize(), properties.getRag().getChunkOverlap());
	}

	@Bean
	@ConditionalOnMissingBean
	PromptTemplateEngine promptTemplateEngine(LlmProperties properties) {
		return new PromptTemplateEngine(properties.getRag().getSystemPrompt());
	}

	@Bean
	@ConditionalOnMissingBean
	VectorStoreService vectorStoreService(VectorStore vectorStore) {
		return new VectorStoreService(vectorStore);
	}

	@Bean
	@ConditionalOnMissingBean
	RagPipeline ragPipeline(LlmRouter llmRouter, VectorStoreService vectorStoreService,
			PromptTemplateEngine promptTemplateEngine, LlmProperties properties) {
		return new RagPipeline(llmRouter, vectorStoreService, promptTemplateEngine,
				properties.getRag().getTopK(), properties.getRag().getSimilarityThreshold());
	}

}
