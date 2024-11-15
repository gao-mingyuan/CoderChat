package com.gmy.coder.chat.router.server;

import com.gmy.coder.chat.api.websocket.constant.NettyConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
public class NettyServer {
    public static final int WEB_SOCKET_PORT = NettyConstant.WEB_SOCKET_PORT;

    private ServerBootstrap serverBootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @PostConstruct
    public void init() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            // 创建 ServerBootstrap
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 添加 StringDecoder 和 StringEncoder 用于处理字符串消息
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            // 添加自定义的 ChannelHandler
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            serverBootstrap.bind(WEB_SOCKET_PORT).sync();
            log.info("NettyWebSocketServer initialized successfully.");
        } catch (Exception e) {
            log.error("Failed to initialize NettyWebSocketServer", e);
            //todo netty服务端启动失败告警
            throw new RuntimeException();
        }
    }

    @PreDestroy
    public void destroy() {
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
    }
}
