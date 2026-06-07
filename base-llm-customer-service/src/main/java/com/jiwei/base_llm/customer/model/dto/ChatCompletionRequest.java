package com.jiwei.base_llm.customer.model.dto;

import com.jiwei.base_llm.customer.model.entity.ChannelType;
import jakarta.validation.constraints.NotBlank;

public record ChatCompletionRequest(
		@NotBlank String message,
		String sessionId,
		String userId,
		ChannelType channel,
		boolean stream
) {

	public ChatCompletionRequest {
		if (channel == null) {
			channel = ChannelType.WEB;
		}
		if (userId == null || userId.isBlank()) {
			userId = "anonymous";
		}
	}

}
