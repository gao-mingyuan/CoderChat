package com.gmy.coder.chat.websocket.client;

import com.gmy.coder.chat.websocket.domain.enums.ConnectionState;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author gaomingyuan
 */
@Slf4j
@Component
public class NettyClientService {

    /**
     * 所有在线路由连接
     */
    private final ConcurrentHashMap<String, ConnectionState> ONLINE_ROUTER = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, ConnectionState> getOnlineRouter() {
        return ONLINE_ROUTER;
    }

    /**
     * 连接成功 修改路由连接状态
     */
    public void added(Channel channel) {
        InetSocketAddress clientAddress = (InetSocketAddress) channel.remoteAddress();
        String ip = clientAddress.getAddress().getHostAddress();
        this.ONLINE_ROUTER.replace(ip, ConnectionState.CONNECTING, ConnectionState.CONNECTED);
    }

    /**
     * 连接关闭 移除在线路由
     */
    public void removed(Channel channel) {
        InetSocketAddress clientAddress = (InetSocketAddress) channel.remoteAddress();
        String ip = clientAddress.getAddress().getHostAddress();
        this.ONLINE_ROUTER.remove(ip);
    }
}
