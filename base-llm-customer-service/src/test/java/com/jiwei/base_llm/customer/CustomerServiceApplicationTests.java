package com.jiwei.base_llm.customer;

import com.jiwei.base_llm.customer.config.TestAiConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestAiConfig.class)
class CustomerServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
