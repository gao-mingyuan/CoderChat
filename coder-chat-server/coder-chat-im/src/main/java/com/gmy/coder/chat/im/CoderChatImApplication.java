package com.gmy.coder.chat.im;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gmy.coder.chat.im")
@EnableDubbo
public class CoderChatImApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoderChatImApplication.class, args);
    }

}
