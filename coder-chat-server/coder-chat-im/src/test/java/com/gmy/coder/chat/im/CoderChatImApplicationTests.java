package com.gmy.coder.chat.im;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CoderChatImApplicationTests {

	@Value("${server.port}")
	String port;
	@Test
	void contextLoads() {
        System.out.println(port);
	}

}
