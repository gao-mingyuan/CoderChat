package com.gmy.coder.chat.websocket.server;

import cn.hutool.json.JSONUtil;
import com.gmy.coder.chat.api.websocket.constant.WSReqTypeEnum;
import com.gmy.coder.chat.api.websocket.request.WSBaseReq;
import com.gmy.coder.chat.netty.util.NettyUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import io.netty.channel.ChannelHandler.Sharable;

@Slf4j
@Component
@Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Resource
    private NettyServerService nettyServerService;

    /**
     * 连接建立完成
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String token = NettyUtil.getAttr(ctx.channel(), NettyUtil.TOKEN);
        this.nettyServerService.online(ctx.channel(), token);
    }

    /**
     * channel关闭时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        userOffLine(ctx);
    }

    /**
     * 用户下线
     */
    private void userOffLine(ChannelHandlerContext ctx) {
        this.nettyServerService.offline(ctx.channel());
        ctx.channel().close();
    }

    /**
     * 心跳检查
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent idleStateEvent) {
            // 读空闲
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                // 关闭用户的连接
                userOffLine(ctx);
            }
        } else if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            String token = NettyUtil.getAttr(ctx.channel(), NettyUtil.TOKEN);
//            if (StrUtil.isBlank(token)) {
//                log.error("token为空,断开连接");
//                ctx.channel().close();
//            }
        }
        super.userEventTriggered(ctx, evt);
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
     * 读取客户端发送的请求报文
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        WSBaseReq wsBaseReq = JSONUtil.toBean(msg.text(), WSBaseReq.class);
        WSReqTypeEnum wsReqTypeEnum = WSReqTypeEnum.of(wsBaseReq.getType());
        switch (wsReqTypeEnum) {
            case HEARTBEAT:
                break;
            case MESSAGE:
                //测试
                this.nettyServerService.sendMessage(msg.text());
//                ctx.channel().writeAndFlush(new TextWebSocketFrame(msg.text()));//测试
                //todo 调用im服务dubbo接口将消息入库
                break;
            default:
                log.info("未知类型");
        }
    }
}
