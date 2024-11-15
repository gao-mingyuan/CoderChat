package com.gmy.coder.chat.im.client;

import com.gmy.coder.chat.api.websocket.constant.NettyConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author gaomingyuan
 */
@Slf4j
@Component
public class NettyWebSocketClient {
    public static final int WEB_SOCKET_PORT = NettyConstant.WEB_SOCKET_PORT;

    private Bootstrap bootstrap;
    private EventLoopGroup group;

    @PostConstruct
    public void init() {
        group = new NioEventLoopGroup();
        try {
            // 创建 Bootstrap
            bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 添加 StringDecoder 和 StringEncoder 用于处理字符串消息
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            // 添加自定义的 ChannelHandler
                            pipeline.addLast(new NettyClientHandler());
                        }
                    });
            log.info("NettyWebSocketClient initialized successfully.");
        } catch (Exception e) {
            log.error("Failed to initialize NettyWebSocketClient", e);
            //todo netty客户端启动失败告警
            throw new RuntimeException();
        }
    }

    public void connect(String host, int port) {
        try {
            log.info("Connecting to server at {}:{}", host, port);
            // 连接到服务器
            ChannelFuture connectFuture = bootstrap.connect(host, port).sync();
            log.info("Connected to server at {}:{}", host, port);
            // 等待客户端 Channel 关闭
            connectFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("Failed to connect to server at {}:{}", host, port, e);
        }
    }

    @PreDestroy
    public void destroy() {
        if (group != null) {
            group.shutdownGracefully();
        }
    }
}
