package com.gmy.coder.chat.router;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.gmy.coder.chat.base.constant.AppNameConstant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class CoderChatRouterApplicationTests {

    @Resource
    private NacosDiscoveryProperties nacosDiscoveryProperties;
    private NamingService namingService;

    @Test
    void contextLoads() throws NacosException {
        this.namingService = NacosFactory.createNamingService(nacosDiscoveryProperties.getNacosProperties());
        List<Instance> instances = this.namingService.getAllInstances(AppNameConstant.WEBSOCKET);
        log.info(JSONObject.toJSONString(instances));
    }
}
