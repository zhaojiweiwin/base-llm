package com.jiwei.base_llm.customer.service.chat;

import com.jiwei.base_llm.customer.model.dto.FeedbackRequest;
import com.jiwei.base_llm.customer.model.entity.ChatFeedbackEntity;
import com.jiwei.base_llm.customer.repository.ChatFeedbackRepository;
import com.jiwei.base_llm.customer.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackService {

	private final ChatFeedbackRepository feedbackRepository;

	private final ChatMessageRepository messageRepository;

	public FeedbackService(ChatFeedbackRepository feedbackRepository, ChatMessageRepository messageRepository) {
		this.feedbackRepository = feedbackRepository;
		this.messageRepository = messageRepository;
	}

	@Transactional
	public void submit(FeedbackRequest request) {
		messageRepository.findById(request.messageId())
				.orElseThrow(() -> new IllegalArgumentException("Message not found: " + request.messageId()));

		ChatFeedbackEntity feedback = new ChatFeedbackEntity();
		feedback.setMessageId(request.messageId());
		feedback.setHelpful(request.helpful());
		feedback.setComment(request.comment());
		feedbackRepository.save(feedback);
	}

}
