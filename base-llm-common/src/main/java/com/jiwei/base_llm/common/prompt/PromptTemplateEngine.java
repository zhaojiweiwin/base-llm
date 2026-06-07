package com.jiwei.base_llm.common.prompt;

import com.jiwei.base_llm.common.model.RetrievedChunk;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.List;
import java.util.stream.Collectors;

public class PromptTemplateEngine {

	private final String systemPrompt;

	public PromptTemplateEngine(String systemPrompt) {
		this.systemPrompt = systemPrompt;
	}

	public Prompt buildPrompt(String query, List<RetrievedChunk> chunks, List<String> history) {
		String context = chunks.stream()
				.map(this::formatCitation)
				.collect(Collectors.joining("\n\n"));

		StringBuilder userContent = new StringBuilder();
		if (!history.isEmpty()) {
			userContent.append("对话历史:\n");
			history.forEach(line -> userContent.append("- ").append(line).append('\n'));
			userContent.append('\n');
		}

		userContent.append("知识库上下文:\n");
		if (context.isBlank()) {
			userContent.append("（未检索到相关内容）\n");
		}
		else {
			userContent.append(context).append('\n');
		}
		userContent.append("\n用户问题: ").append(query);

		return new Prompt(List.of(new SystemMessage(systemPrompt), new UserMessage(userContent.toString())));
	}

	private String formatCitation(RetrievedChunk chunk) {
		return """
				[来源: %s | 章节: %s | 链接: %s]
				%s
				""".formatted(
				chunk.documentTitle() != null ? chunk.documentTitle() : "未知文档",
				chunk.sectionPath() != null ? chunk.sectionPath() : "root",
				chunk.sourceUrl() != null ? chunk.sourceUrl() : "N/A",
				chunk.content());
	}

}
