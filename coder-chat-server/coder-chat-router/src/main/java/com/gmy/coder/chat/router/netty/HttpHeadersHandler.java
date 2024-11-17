package com.gmy.coder.chat.router.netty;

import cn.hutool.core.net.url.UrlBuilder;
import com.gmy.coder.chat.netty.util.NettyUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;

@Slf4j
public class HttpHeadersHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(request.uri());

            // 获取serverId
            String serverId = Optional.ofNullable(urlBuilder.getQuery()).map(k -> k.get("serverId")).map(CharSequence::toString).orElse("");
            NettyUtil.setAttr(ctx.channel(), NettyUtil.SERVER_ID, serverId);

            ctx.pipeline().remove(this);
            //将数据传递给ChannelPipeline中的下一个处理器
            ctx.fireChannelRead(request);
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}