package com.gmy.coder.chat.router.controller;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import com.gmy.coder.chat.base.constant.AppNameConstant;
import com.gmy.coder.chat.router.netty.NettyServerService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {
    @Resource
    private NettyServerService nettyServerService;
    @Resource
    private Registration registration;

    @GetMapping("/test")
    public void test(String msg) throws NacosException {
        ConcurrentHashMap<String, Channel> map = this.nettyServerService.getOnlineChannel();
        map.forEach((k, v) -> {
            v.writeAndFlush(new TextWebSocketFrame(msg));
        });
        log.info("metadata:{}", JSONObject.toJSONString(registration.getMetadata()));
        registration.getMetadata().put("id", AppNameConstant.SERVER_ID);
        log.info("metadata:{}", JSONObject.toJSONString(registration.getMetadata()));
    }
}
