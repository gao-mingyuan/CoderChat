package com.gmy.coder.chat.websocket.client;

import com.gmy.coder.chat.api.websocket.constant.NettyConstant;
import com.gmy.coder.chat.base.constant.AppNameConstant;
import com.gmy.coder.chat.websocket.domain.enums.ConnectionState;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

/**
 * netty客户端 连接路由服务
 *
 * @author gaomingyuan
 */
@Slf4j
@Component
public class NettyClient {
    @Resource
    private NettyClientService nettyClientService;
    @Resource
    public NettyClientHandler clientHandler;
    private Bootstrap bootstrap;
    private EventLoopGroup group;

    public void connect(String host) {
        ConcurrentHashMap<String, ConnectionState> routerMap = this.nettyClientService.getOnlineRouter();

        boolean pass = routerMap.replace(host, ConnectionState.WAITING_FOR_CONNECTION, ConnectionState.CONNECTING);
        if (!pass) {
            return;
        }
        log.info("连接中,server at {}", host);

        // 这里构造 WebSocket 的完整 URL
        String webSocketUrl = "ws://" + host + ":" + NettyConstant.NETTY_PORT + "?serverId=" + AppNameConstant.SERVER_ID;
        WebSocketClientProtocolHandler webSocketClientProtocolHandler = this.createWebSocketClientProtocolHandler(webSocketUrl);
        bootstrap.connect(host, NettyConstant.NETTY_PORT).addListener((ChannelFuture future) -> {
            if (future.isSuccess()) {
                log.info("连接成功,server at {}", host);
                future.channel().pipeline().addLast(webSocketClientProtocolHandler);
                webSocketClientProtocolHandler.handshaker().handshake(future.channel());
                // 连接成功后的逻辑处理
            } else {
                log.error("连接失败,server at {}", host, future.cause());
                // 连接失败后的逻辑处理
                routerMap.compute(host, (k, v) -> {
                    if (v == ConnectionState.CONNECTING) {
                        return null; // 移除键值对
                    } else {
                        return v; // 保持原状态
                    }
                });

            }
        });
    }

    private WebSocketClientProtocolHandler createWebSocketClientProtocolHandler(String webSocketUrl) {
        return new WebSocketClientProtocolHandler(
                WebSocketClientHandshakerFactory.newHandshaker(
                        URI.create(webSocketUrl),
                        io.netty.handler.codec.http.websocketx.WebSocketVersion.V13,
                        null,
                        false,
                        new io.netty.handler.codec.http.DefaultHttpHeaders()));
    }

    @PostConstruct
    public void init() {
        this.group = new NioEventLoopGroup();
        // 创建 Bootstrap
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(0, 10, 0));
                        // 添加 HTTP 编码和解码器
                        pipeline.addLast(new HttpClientCodec());
                        pipeline.addLast(new HttpObjectAggregator(8192)); // 聚合 HTTP 消息
                        // 添加自定义的 ChannelHandler
                        pipeline.addLast(clientHandler);
                    }
                });
    }

    @PreDestroy
    public void destroy() {
        if (group != null) {
            group.shutdownGracefully().syncUninterruptibly();
        }
    }
}
