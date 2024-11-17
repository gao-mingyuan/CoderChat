package com.gmy.coder.chat.router.netty;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author gaomingyuan
 */
@Slf4j
@Sharable
@Component
public class NettyServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Resource
    private NettyServerService nettyServerService;

    /**
     * 心跳检查
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent idleStateEvent) {
            // 读空闲
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                ctx.channel().close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 连接建立完成
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.nettyServerService.added(ctx.channel());
    }

    /**
     * 处理异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("异常发生,异常消息:{}", cause.getMessage(), cause);
        ctx.channel().close();
    }

    /**
     * channel关闭时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.nettyServerService.removed(ctx.channel());
    }

    /**
     * 处理从客户端接收到的消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //目前websocket服务只会往router服务发心跳包,所以不做处理
    }
}
