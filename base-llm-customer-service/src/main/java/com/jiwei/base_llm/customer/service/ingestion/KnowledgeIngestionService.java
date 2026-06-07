package com.jiwei.base_llm.customer.service.ingestion;

import com.jiwei.base_llm.common.model.DocumentChunk;
import com.jiwei.base_llm.common.parser.DocumentChunker;
import com.jiwei.base_llm.common.parser.DocumentParser;
import com.jiwei.base_llm.common.vector.VectorStoreService;
import com.jiwei.base_llm.customer.model.dto.DocumentResponse;
import com.jiwei.base_llm.customer.model.entity.DocumentStatus;
import com.jiwei.base_llm.customer.model.entity.KbDocumentEntity;
import com.jiwei.base_llm.customer.model.entity.SourceType;
import com.jiwei.base_llm.customer.repository.KbDocumentRepository;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KnowledgeIngestionService {

	private final KbDocumentRepository documentRepository;

	private final DocumentParser documentParser;

	private final DocumentChunker documentChunker;

	private final VectorStoreService vectorStoreService;

	public KnowledgeIngestionService(KbDocumentRepository documentRepository, DocumentParser documentParser,
			DocumentChunker documentChunker, VectorStoreService vectorStoreService) {
		this.documentRepository = documentRepository;
		this.documentParser = documentParser;
		this.documentChunker = documentChunker;
		this.vectorStoreService = vectorStoreService;
	}

	@Transactional
	public DocumentResponse upload(MultipartFile file) throws IOException {
		KbDocumentEntity document = new KbDocumentEntity();
		document.setTitle(file.getOriginalFilename());
		document.setSourceType(SourceType.FILE);
		document.setFilePath(file.getOriginalFilename());
		document.setStatus(DocumentStatus.INDEXING);
		document = documentRepository.save(document);

		try {
			String content = documentParser.parse(file.getInputStream(), file.getOriginalFilename());
			indexDocument(document, content);
			document.setStatus(DocumentStatus.INDEXED);
			document.setSyncedAt(Instant.now());
			documentRepository.save(document);
			return toResponse(document);
		}
		catch (Exception ex) {
			document.setStatus(DocumentStatus.FAILED);
			documentRepository.save(document);
			throw ex;
		}
	}

	public List<DocumentResponse> listDocuments() {
		return documentRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Transactional
	public void deleteDocument(String documentId) {
		documentRepository.findById(documentId).ifPresent(document -> {
			vectorStoreService.deleteByFilter("documentId == '" + documentId + "'");
			documentRepository.delete(document);
		});
	}

	private void indexDocument(KbDocumentEntity document, String content) {
		vectorStoreService.deleteByFilter("documentId == '" + document.getId() + "'");

		Map<String, Object> baseMetadata = new HashMap<>();
		baseMetadata.put("documentId", document.getId());
		baseMetadata.put("documentTitle", document.getTitle());
		baseMetadata.put("sourceType", document.getSourceType().name());
		baseMetadata.put("sourceUrl", document.getSourceUrl());

		List<DocumentChunk> chunks = documentChunker.chunk(content, baseMetadata);
		List<Document> documents = chunks.stream()
				.map(chunk -> VectorStoreService.toDocument(chunk.content(), chunk.metadata()))
				.toList();

		vectorStoreService.addDocuments(documents);
		document.setChunkCount(chunks.size());
	}

	private DocumentResponse toResponse(KbDocumentEntity document) {
		return new DocumentResponse(document.getId(), document.getTitle(), document.getSourceType(),
				document.getSourceUrl(), document.getStatus(), document.getChunkCount(), document.getSyncedAt());
	}

}
