package com.jiwei.base_llm.customer.controller;

import com.jiwei.base_llm.customer.model.dto.DocumentResponse;
import com.jiwei.base_llm.customer.service.ingestion.KnowledgeIngestionService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/knowledge")
public class KnowledgeController {

	private final KnowledgeIngestionService ingestionService;

	public KnowledgeController(KnowledgeIngestionService ingestionService) {
		this.ingestionService = ingestionService;
	}

	@PostMapping("/documents/upload")
	public DocumentResponse upload(@RequestParam("file") MultipartFile file) throws IOException {
		return ingestionService.upload(file);
	}

	@GetMapping("/documents")
	public List<DocumentResponse> listDocuments() {
		return ingestionService.listDocuments();
	}

	@DeleteMapping("/documents/{id}")
	public void deleteDocument(@PathVariable String id) {
		ingestionService.deleteDocument(id);
	}

}
