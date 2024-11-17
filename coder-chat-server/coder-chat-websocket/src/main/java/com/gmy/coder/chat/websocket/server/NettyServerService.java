package com.gmy.coder.chat.websocket.server;

import cn.hutool.core.collection.CollectionUtil;
import com.gmy.coder.chat.netty.util.NettyUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @author gaomingyuan
 */
@Slf4j
@Component
public class NettyServerService {

    /**
     * 所有在线用户
     */
    private final ConcurrentHashMap<Integer, CopyOnWriteArrayList<Channel>> ONLINE_USER_MAP = new ConcurrentHashMap<>();


    /**
     * 用户上线
     */
    public void online(Channel channel, String token) {
        //todo 完成auth模块 通过token查到id
        Integer uid = (int) Math.random() * 100;
        this.ONLINE_USER_MAP.putIfAbsent(uid, new CopyOnWriteArrayList<>());
        this.ONLINE_USER_MAP.get(uid).add(channel);
        NettyUtil.setAttr(channel, NettyUtil.UID, uid);
        log.info("当前用户数:{}", ONLINE_USER_MAP.size());
        //todo 1.用户连接到哪台服务更新到redis, 2.上线推送
    }

    /**
     * 用户下线
     */
    public void offline(Channel channel) {
        Integer uid = NettyUtil.getAttr(channel, NettyUtil.UID);
        CopyOnWriteArrayList<Channel> channels = this.ONLINE_USER_MAP.get(uid);
        if (CollectionUtil.isNotEmpty(channels)) {
            channels.removeIf(ch -> Objects.equals(ch, channel));
        }

        if (CollectionUtil.isEmpty(channels)) {
            Object value = this.ONLINE_USER_MAP.compute(uid, (k, v) -> {
                if (CollectionUtil.isEmpty(channels)) {
                    return null; // 移除键值对
                } else {
                    return v; // 保持原状态
                }
            });

            //如果value为空 表示用户所有端通道都已关闭
            if (Objects.isNull(value)) {
                //todo 下线推送
            }
        }
        log.info("当前用户数:{}", ONLINE_USER_MAP.size());
    }

    /**
     * 接收路由服务推送消息到客户端
     */
    public void sendMessage(String message) {
        //todo 根据uid推送
        this.ONLINE_USER_MAP.forEach((k, v) -> {
            v.forEach(channel -> channel.writeAndFlush(message));
        });
    }

}
