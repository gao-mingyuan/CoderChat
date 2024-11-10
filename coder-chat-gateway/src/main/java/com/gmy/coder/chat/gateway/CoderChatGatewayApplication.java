package com.gmy.coder.chat.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gmy.coder.chat.gateway")
public class CoderChatGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoderChatGatewayApplication.class, args);
    }

}
