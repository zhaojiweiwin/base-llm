package com.jiwei.base_llm.customer.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;

@Configuration
@Profile("local")
public class LocalVectorStoreConfig {

	@Bean
	@ConditionalOnMissingBean(VectorStore.class)
	@ConditionalOnProperty(prefix = "spring.ai.vectorstore.pgvector", name = "enabled", havingValue = "false", matchIfMissing = true)
	VectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
		return SimpleVectorStore.builder(embeddingModel).build();
	}

}
