package com.gmy.coder.chat.im.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gaomingyuan
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 连接建立完成
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接建立完成");
        // 当 Channel 激活时，发送一条消息到服务器
        ctx.writeAndFlush("Hello, Server!");
        //todo 缓存channel
    }

    /**
     * 处理从服务器接收到的消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("Received from server msg:{} ", msg);
        //todo 找到用户通道并转发消息
    }

    /**
     * 处理异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("处理异常,msg:{} ", cause.getMessage());
        // 处理异常
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * channel关闭时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //todo 清除channel缓存
        log.info("Channel close: {}", ctx.channel().remoteAddress());
    }
}
