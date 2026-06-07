package com.jiwei.base_llm.customer.service.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiwei.base_llm.common.model.RagRequest;
import com.jiwei.base_llm.common.model.RagResponse;
import com.jiwei.base_llm.common.rag.RagPipeline;
import com.jiwei.base_llm.customer.model.dto.ChatCompletionRequest;
import com.jiwei.base_llm.customer.model.dto.ChatCompletionResponse;
import com.jiwei.base_llm.customer.model.entity.ChannelType;
import com.jiwei.base_llm.customer.model.entity.ChatMessageEntity;
import com.jiwei.base_llm.customer.model.entity.ChatSessionEntity;
import com.jiwei.base_llm.customer.model.entity.MessageRole;
import com.jiwei.base_llm.customer.repository.ChatMessageRepository;
import com.jiwei.base_llm.customer.repository.ChatSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class ChatService {

	private final ChatSessionRepository sessionRepository;

	private final ChatMessageRepository messageRepository;

	private final RagPipeline ragPipeline;

	private final ObjectMapper objectMapper;

	public ChatService(ChatSessionRepository sessionRepository, ChatMessageRepository messageRepository,
			RagPipeline ragPipeline, ObjectMapper objectMapper) {
		this.sessionRepository = sessionRepository;
		this.messageRepository = messageRepository;
		this.ragPipeline = ragPipeline;
		this.objectMapper = objectMapper;
	}

	@Transactional
	public ChatCompletionResponse chat(ChatCompletionRequest request) {
		ChatSessionEntity session = resolveSession(request.sessionId(), request.userId(), request.channel());
		saveMessage(session.getId(), MessageRole.USER, request.message(), null, null);

		List<String> history = loadHistory(session.getId());
		RagResponse ragResponse = ragPipeline.query(new RagRequest(request.message(), history, null));

		ChatMessageEntity assistantMessage = saveMessage(session.getId(), MessageRole.ASSISTANT,
				ragResponse.answer(), ragResponse.citations(), ragResponse.confidence());

		return new ChatCompletionResponse(session.getId(), assistantMessage.getId(), ragResponse.answer(),
				ragResponse.citations(), ragResponse.confidence(), ragResponse.lowConfidence());
	}

	@Transactional
	public Flux<String> stream(ChatCompletionRequest request) {
		ChatSessionEntity session = resolveSession(request.sessionId(), request.userId(), request.channel());
		saveMessage(session.getId(), MessageRole.USER, request.message(), null, null);

		List<String> history = loadHistory(session.getId());
		StringBuilder answerBuilder = new StringBuilder();

		return ragPipeline.stream(new RagRequest(request.message(), history, null))
				.doOnNext(answerBuilder::append)
				.doOnComplete(() -> saveMessage(session.getId(), MessageRole.ASSISTANT, answerBuilder.toString(), null,
						null));
	}

	public List<ChatMessageEntity> getSessionMessages(String sessionId) {
		return messageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
	}

	private ChatSessionEntity resolveSession(String sessionId, String userId, ChannelType channel) {
		if (sessionId != null && !sessionId.isBlank()) {
			return sessionRepository.findById(sessionId)
					.orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));
		}

		ChatSessionEntity session = new ChatSessionEntity();
		session.setUserId(userId);
		session.setChannel(channel);
		return sessionRepository.save(session);
	}

	private List<String> loadHistory(String sessionId) {
		return messageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId).stream()
				.map(message -> message.getRole().name() + ": " + message.getContent())
				.toList();
	}

	private ChatMessageEntity saveMessage(String sessionId, MessageRole role, String content, Object citations,
			Double confidence) {
		ChatMessageEntity message = new ChatMessageEntity();
		message.setSessionId(sessionId);
		message.setRole(role);
		message.setContent(content);
		message.setConfidence(confidence);
		if (citations != null) {
			try {
				message.setCitations(objectMapper.writeValueAsString(citations));
			}
			catch (JsonProcessingException ex) {
				message.setCitations("[]");
			}
		}
		return messageRepository.save(message);
	}

}
