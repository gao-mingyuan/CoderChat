package com.gmy.coder.chat.router.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gaomingyuan
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 连接建立完成
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Client connected: {}", ctx.channel().remoteAddress());
    }

    /**
     * 处理从客户端接收到的消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("Received from client msg: {}", msg);
        // 回复客户端
        ctx.writeAndFlush("Hello, Client! You said: " + msg);
    }

    /**
     * 处理异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Exception caught in channel: {}", cause.getMessage(), cause);
        ctx.close();
    }

    /**
     * channel关闭时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Client disconnected: {}", ctx.channel().remoteAddress());
    }
}
