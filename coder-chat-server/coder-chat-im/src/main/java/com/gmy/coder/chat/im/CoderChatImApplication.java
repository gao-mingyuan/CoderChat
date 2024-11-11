package com.gmy.coder.chat.im;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.gmy.coder.chat.im")
public class CoderChatImApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoderChatImApplication.class, args);
    }

}
