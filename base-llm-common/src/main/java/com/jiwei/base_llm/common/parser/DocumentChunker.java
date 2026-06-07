package com.jiwei.base_llm.common.parser;

import com.jiwei.base_llm.common.model.DocumentChunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentChunker {

	private static final Pattern HEADING_PATTERN = Pattern.compile("^(#{1,6}\\s+.+)$", Pattern.MULTILINE);

	private final int chunkSize;

	private final int chunkOverlap;

	public DocumentChunker(int chunkSize, int chunkOverlap) {
		this.chunkSize = chunkSize;
		this.chunkOverlap = chunkOverlap;
	}

	public List<DocumentChunk> chunk(String content, Map<String, Object> baseMetadata) {
		List<String> sections = splitByHeadings(content);
		List<DocumentChunk> chunks = new ArrayList<>();
		int index = 0;

		for (String section : sections) {
			String sectionPath = extractSectionPath(section);
			List<String> sectionChunks = splitWithOverlap(section, chunkSize, chunkOverlap);
			for (String sectionChunk : sectionChunks) {
				Map<String, Object> metadata = new HashMap<>(baseMetadata);
				metadata.put("sectionPath", sectionPath);
				metadata.put("chunkIndex", index);
				chunks.add(new DocumentChunk(sectionChunk.trim(), index, sectionPath, metadata));
				index++;
			}
		}

		if (chunks.isEmpty() && !content.isBlank()) {
			Map<String, Object> metadata = new HashMap<>(baseMetadata);
			metadata.put("sectionPath", "root");
			metadata.put("chunkIndex", 0);
			chunks.add(new DocumentChunk(content.trim(), 0, "root", metadata));
		}

		return chunks;
	}

	private List<String> splitByHeadings(String content) {
		Matcher matcher = HEADING_PATTERN.matcher(content);
		List<String> sections = new ArrayList<>();
		int lastStart = 0;

		while (matcher.find()) {
			if (matcher.start() > lastStart) {
				sections.add(content.substring(lastStart, matcher.start()));
			}
			lastStart = matcher.start();
		}

		if (lastStart < content.length()) {
			sections.add(content.substring(lastStart));
		}

		return sections.stream().filter(section -> !section.isBlank()).toList();
	}

	private String extractSectionPath(String section) {
		int newlineIndex = section.indexOf('\n');
		String firstLine = newlineIndex >= 0 ? section.substring(0, newlineIndex) : section;
		return firstLine.startsWith("#") ? firstLine.replaceAll("^#+\\s*", "").trim() : "root";
	}

	private List<String> splitWithOverlap(String text, int size, int overlap) {
		if (text.length() <= size) {
			return List.of(text);
		}

		List<String> chunks = new ArrayList<>();
		int start = 0;
		while (start < text.length()) {
			int end = Math.min(start + size, text.length());
			chunks.add(text.substring(start, end));
			if (end >= text.length()) {
				break;
			}
			start = Math.max(end - overlap, start + 1);
		}
		return chunks;
	}

}
