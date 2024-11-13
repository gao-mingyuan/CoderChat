package com.gmy.coder.chat.im.controller;


import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.NacosNamingService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/im/message")
public class ApiController {

    @Value("${server.port}")
    String port;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    NacosNamingService nacosNamingService = null;

    @PostConstruct
    public void init() throws NacosException {
        /**
         * 构建获取nacos服务信息
         */
        nacosNamingService = new NacosNamingService(nacosDiscoveryProperties.getNacosProperties());
    }

    @GetMapping("/test")
    public Object test() throws NacosException {
        List<Instance> instances = nacosNamingService.getAllInstances("coder-chat-im");
        log.info("instances:{}", JSONObject.toJSONString(instances));
        return port;
    }
}
