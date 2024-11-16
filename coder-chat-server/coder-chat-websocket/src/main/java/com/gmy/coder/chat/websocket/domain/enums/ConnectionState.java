package com.gmy.coder.chat.websocket.domain.enums;

/**
 * 与路由服务连接状态
 *
 * @author gaomingyuan
 */
public enum ConnectionState {
    /**
     * 等待连接
     */
    WAITING_FOR_CONNECTION,
    /**
     * 连接中
     */
    CONNECTING,

    /**
     * 已连接
     */
    CONNECTED,

    ;
}
