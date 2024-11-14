package com.gmy.coder.chat.gateway.routes;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.NacosNamingService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.List;

/**
 * @author gaomingyuan
 */
@Component
@Slf4j
public class WebSocketInstallRoutingFilter extends AbstractGatewayFilterFactory{

    private final NacosNamingService nacosNamingService;

    public WebSocketInstallRoutingFilter(NacosDiscoveryProperties nacosDiscoveryProperties) throws NacosException {
        this.nacosNamingService = new NacosNamingService(nacosDiscoveryProperties.getNacosProperties());
    }

    @Override
    public GatewayFilter apply(Object config) {
        log.info("1111");
        return (exchange, chain) -> {
            try {
                List<Instance> instances = nacosNamingService.getAllInstances("coder-chat-websocket");

                // 选择一个实例，这里简单选择第一个
                Instance selectedInstance = instances.isEmpty() ? null : instances.get(0);

                // 如果没有找到实例，可以抛出异常或返回错误
                if (selectedInstance == null) {
                    throw new RuntimeException("No instances available for service coder-chat-websocket");
                }

                // 构建新的 WebSocket URI
                String serviceUri = "ws://" + selectedInstance.getIp() + ":8090";

                // 修改请求 URI
                ServerWebExchange mutatedExchange = exchange.mutate()
                        .request(exchange.getRequest().mutate().uri(URI.create(serviceUri)).build())
                        .build();

                // 继续过滤链
                return chain.filter(mutatedExchange);
            } catch (NacosException e) {
                throw new RuntimeException(e);
            }

        };
    }


    @Override
    public String name() {
        return "WebSocketInstallRoutingFilter"; // 过滤器名称
    }
}
