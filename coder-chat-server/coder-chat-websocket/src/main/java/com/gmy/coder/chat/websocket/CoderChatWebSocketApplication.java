package com.gmy.coder.chat.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.gmy.coder.chat.websocket")
@EnableScheduling
public class CoderChatWebSocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoderChatWebSocketApplication.class, args);
    }

}
