package com.gmy.coder.chat.gateway.filter;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.gmy.coder.chat.api.websocket.constant.NettyConstant;
import com.gmy.coder.chat.base.constant.AppNameConstant;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

/**
 * @author gaomingyuan
 */
@Component
@Slf4j
public class WebSocketInstallRoutingFilter extends AbstractGatewayFilterFactory<WebSocketInstallRoutingFilter.Config> {

    private NacosNamingService nacosNamingService = null;

    @Resource
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @PostConstruct
    private void init() throws NacosException {
        this.nacosNamingService = new NacosNamingService(nacosDiscoveryProperties.getNacosProperties());
    }


    public WebSocketInstallRoutingFilter() {
        super(Config.class);
    }

    @Override
    public Config newConfig() {
        return super.newConfig();
    }

    public static class Config {
        // 可以定义一些配置属性
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            try {
                List<Instance> instances = nacosNamingService.getAllInstances(AppNameConstant.WEBSOCKET);

                //todo 选择连接数最少的, 如果用户多端登录,分配到同一个服务
                Instance selectedInstance = instances.isEmpty() ? null : instances.get(0);

                //如果没有找到实例，可以抛出异常或返回错误
                if (selectedInstance == null) {
                    throw new RuntimeException("No instances available for service coder-chat-websocket");
                }

                //构建新的 WebSocket URI
                String serviceUri = "ws://" + selectedInstance.getIp() + ":" + NettyConstant.NETTY_PORT;

                //获取Route信息并修改uri
                Route currentRoute = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
                URI newUri = URI.create(serviceUri);
                Route updatedRoute = Route.async()
                        .id(currentRoute.getId())
                        .uri(newUri)
                        .order(currentRoute.getOrder())
                        .asyncPredicate(currentRoute.getPredicate())
                        .build();
                exchange.getAttributes().put(GATEWAY_ROUTE_ATTR, updatedRoute);

                // 继续过滤链
                return chain.filter(exchange);
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
