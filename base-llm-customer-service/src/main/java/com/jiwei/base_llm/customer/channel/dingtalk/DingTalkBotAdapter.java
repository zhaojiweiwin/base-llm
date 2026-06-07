package com.jiwei.base_llm.customer.channel.dingtalk;

import com.jiwei.base_llm.customer.config.DingTalkProperties;
import com.jiwei.base_llm.customer.model.dto.ChatCompletionRequest;
import com.jiwei.base_llm.customer.model.dto.ChatCompletionResponse;
import com.jiwei.base_llm.customer.model.entity.ChannelType;
import com.jiwei.base_llm.customer.service.chat.ChatService;
import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DingTalkBotAdapter {

	private static final Logger log = LoggerFactory.getLogger(DingTalkBotAdapter.class);

	private static final String ROBOT_TOPIC = "/v1.0/robot/message";

	private final DingTalkProperties properties;

	private final ChatService chatService;

	public DingTalkBotAdapter(DingTalkProperties properties, ChatService chatService) {
		this.properties = properties;
		this.chatService = chatService;
	}

	@PostConstruct
	public void start() {
		log.info("Starting DingTalk Stream bot");
		try {
			OpenDingTalkStreamClientBuilder.custom()
					.credential(new AuthClientCredential(properties.getClientId(), properties.getClientSecret()))
					.registerCallbackListener(ROBOT_TOPIC, message -> {
						String userId = extractUserId(message);
						String content = extractContent(message);
						log.info("Received DingTalk message from user={}, content={}", userId, content);

						ChatCompletionResponse response = chatService.chat(new ChatCompletionRequest(content, null,
								userId, ChannelType.DINGTALK, false));

						return buildReply(response.answer());
					})
					.build()
					.start();
		}
		catch (Exception ex) {
			throw new IllegalStateException("Failed to start DingTalk Stream client", ex);
		}
	}

	private String extractUserId(Object message) {
		return message == null ? "dingtalk-user" : message.toString();
	}

	private String extractContent(Object message) {
		return message == null ? "" : message.toString();
	}

	private Object buildReply(String answer) {
		return answer;
	}

}
