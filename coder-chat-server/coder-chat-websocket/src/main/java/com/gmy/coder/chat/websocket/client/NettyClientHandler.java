package com.gmy.coder.chat.websocket.client;

import com.gmy.coder.chat.api.router.constant.RouterReqTypeEnum;
import com.gmy.coder.chat.websocket.server.NettyServerService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * @author gaomingyuan
 */
@Slf4j
@Component
@Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Resource
    private NettyClientService nettyClientService;
    @Resource
    private NettyServerService nettyServerService;

    /**
     * 连接建立完成
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.nettyClientService.added(ctx.channel());
    }

    /**
     * 心跳检查
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event) {
            if (event.state() == IdleState.WRITER_IDLE) {
                // 发送心跳包给Router模块
                ctx.writeAndFlush(new TextWebSocketFrame(String.valueOf(RouterReqTypeEnum.HEARTBEAT.getType())));
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 处理从路由服务器接收到的消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        this.nettyServerService.sendMessage("路由服务器说:" + msg.text());
        //todo 找到用户通道并转发消息
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
        this.nettyClientService.removed(ctx.channel());
    }
}
