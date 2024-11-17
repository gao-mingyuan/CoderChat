package com.gmy.coder.chat.websocket.listener;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.gmy.coder.chat.base.constant.AppNameConstant;
import com.gmy.coder.chat.websocket.client.NettyClient;
import com.gmy.coder.chat.websocket.client.NettyClientService;
import com.gmy.coder.chat.websocket.domain.enums.ConnectionState;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 监听路由服务 建立连接
 *
 * @author gaomingyuan
 */
@Slf4j
@Component
public class RouterServerListener {
    @Resource
    private NettyClient nettyClient;
    @Resource
    private NettyClientService nettyClientService;
    @Resource
    private NacosDiscoveryProperties nacosDiscoveryProperties;
    private NamingService namingService;

    @PostConstruct
    public void init() throws NacosException {
        this.namingService = NacosFactory.createNamingService(nacosDiscoveryProperties.getNacosProperties());
        this.namingService.subscribe(AppNameConstant.ROUTER, event -> {
            if (event instanceof NamingEvent namingEvent) {
                List<Instance> instanceList = namingEvent.getInstances();
                this.tryConnect(instanceList);
            }
        });
    }

    /**
     * 定时任务兜底, 防止监听服务连接失败
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void scanRouterServerConnect() throws NacosException {
        List<Instance> instances = this.namingService.getAllInstances(AppNameConstant.ROUTER);
        this.tryConnect(instances);
    }

    private void tryConnect(List<Instance> instanceList) {
        ConcurrentHashMap<String, ConnectionState> routerMap = this.nettyClientService.getOnlineRouter();
        for (Instance instance : instanceList) {
            String serverName = instance.getIp();
            ConnectionState state = routerMap.computeIfAbsent(serverName, k -> ConnectionState.WAITING_FOR_CONNECTION);
            if (state == ConnectionState.WAITING_FOR_CONNECTION) {
                this.nettyClient.connect(instance.getIp());
            }
        }
    }
}
