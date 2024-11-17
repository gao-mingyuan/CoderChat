package com.gmy.coder.chat.netty.util;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * netty工具类
 *
 * @author gaomingyuan
 */
public class NettyUtil {
    public static AttributeKey<String> SERVER_ID = AttributeKey.valueOf("serverId");
    public static AttributeKey<String> TOKEN = AttributeKey.valueOf("token");
    public static AttributeKey<String> IP = AttributeKey.valueOf("ip");
    public static AttributeKey<Integer> UID = AttributeKey.valueOf("uid");

    public static <T> void setAttr(Channel channel, AttributeKey<T> attributeKey, T data) {
        Attribute<T> attr = channel.attr(attributeKey);
        attr.set(data);
    }

    public static <T> T getAttr(Channel channel, AttributeKey<T> ip) {
        return channel.attr(ip).get();
    }
}
