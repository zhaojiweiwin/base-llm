package com.jiwei.base_llm.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "base-llm")
public class LlmProperties {

	private Rag rag = new Rag();

	public Rag getRag() {
		return rag;
	}

	public void setRag(Rag rag) {
		this.rag = rag;
	}

	public static class Rag {

		private int chunkSize = 800;

		private int chunkOverlap = 128;

		private int topK = 5;

		private double similarityThreshold = 0.6;

		private String systemPrompt = """
				你是研发中心技术运维助手。请仅基于提供的知识库内容回答用户问题。
				如果知识库中没有相关信息，请明确说明不知道，不要编造。
				回答请简洁清晰，并在末尾列出引用来源。
				""";

		public int getChunkSize() {
			return chunkSize;
		}

		public void setChunkSize(int chunkSize) {
			this.chunkSize = chunkSize;
		}

		public int getChunkOverlap() {
			return chunkOverlap;
		}

		public void setChunkOverlap(int chunkOverlap) {
			this.chunkOverlap = chunkOverlap;
		}

		public int getTopK() {
			return topK;
		}

		public void setTopK(int topK) {
			this.topK = topK;
		}

		public double getSimilarityThreshold() {
			return similarityThreshold;
		}

		public void setSimilarityThreshold(double similarityThreshold) {
			this.similarityThreshold = similarityThreshold;
		}

		public String getSystemPrompt() {
			return systemPrompt;
		}

		public void setSystemPrompt(String systemPrompt) {
			this.systemPrompt = systemPrompt;
		}

	}

}
