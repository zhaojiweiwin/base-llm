package com.jiwei.base_llm.customer.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FeedbackRequest(
		@NotBlank String messageId,
		@NotNull Boolean helpful,
		String comment
) {
}
