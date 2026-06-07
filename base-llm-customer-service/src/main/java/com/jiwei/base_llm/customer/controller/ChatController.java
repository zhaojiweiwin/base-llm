package com.jiwei.base_llm.customer.controller;

import com.jiwei.base_llm.customer.model.dto.ChatCompletionRequest;
import com.jiwei.base_llm.customer.model.dto.ChatCompletionResponse;
import com.jiwei.base_llm.customer.model.dto.FeedbackRequest;
import com.jiwei.base_llm.customer.model.entity.ChatMessageEntity;
import com.jiwei.base_llm.customer.service.chat.ChatService;
import com.jiwei.base_llm.customer.service.chat.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

	private final ChatService chatService;

	private final FeedbackService feedbackService;

	public ChatController(ChatService chatService, FeedbackService feedbackService) {
		this.chatService = chatService;
		this.feedbackService = feedbackService;
	}

	@PostMapping("/completions")
	public ChatCompletionResponse completions(@Valid @RequestBody ChatCompletionRequest request) {
		if (request.stream()) {
			throw new IllegalArgumentException("Use /completions/stream for streaming responses");
		}
		return chatService.chat(request);
	}

	@PostMapping(value = "/completions/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> stream(@Valid @RequestBody ChatCompletionRequest request) {
		return chatService.stream(request);
	}

	@GetMapping("/sessions/{sessionId}")
	public List<ChatMessageEntity> sessionHistory(@PathVariable String sessionId) {
		return chatService.getSessionMessages(sessionId);
	}

	@PostMapping("/feedback")
	public void feedback(@Valid @RequestBody FeedbackRequest request) {
		feedbackService.submit(request);
	}

}
