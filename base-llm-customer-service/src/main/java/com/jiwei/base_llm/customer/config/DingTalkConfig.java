package com.jiwei.base_llm.customer.config;

import com.jiwei.base_llm.customer.channel.dingtalk.DingTalkBotAdapter;
import com.jiwei.base_llm.customer.service.chat.ChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DingTalkProperties.class)
public class DingTalkConfig {

	@Bean
	@ConditionalOnProperty(prefix = "dingtalk", name = "enabled", havingValue = "true")
	DingTalkBotAdapter dingTalkBotAdapter(DingTalkProperties properties, ChatService chatService) {
		return new DingTalkBotAdapter(properties, chatService);
	}

}
