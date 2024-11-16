package com.gmy.coder.chat.router.netty;

import com.gmy.coder.chat.netty.util.NettyUtil;
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
public class NettyServerService {

    /**
     * 所有websocket服务
     */
    private final ConcurrentHashMap<String, Channel> ONLINE_CHANNEL_MAP = new ConcurrentHashMap<>();


    /**
     * 服务注册
     */
    public void added(Channel channel) {
        InetSocketAddress clientAddress = (InetSocketAddress) channel.remoteAddress();
        String ip = clientAddress.getAddress().getHostAddress();
        this.ONLINE_CHANNEL_MAP.putIfAbsent(ip, channel);
        NettyUtil.setAttr(channel, NettyUtil.IP, ip);
    }

    /**
     * 服务下线
     */
    public void removed(Channel channel) {
        String ip = NettyUtil.getAttr(channel, NettyUtil.IP);
        this.ONLINE_CHANNEL_MAP.remove(ip);
    }

}
