package com.gmy.coder.chat.base.constant;

import java.util.UUID;

/**
 * @author gaomingyuan
 */
public class AppNameConstant {
    public static final String SERVER_ID = UUID.randomUUID().toString().replace("-", "");

    public static final String BASE_NAME = "coder-chat-";
    public static final String WEBSOCKET = BASE_NAME + "websocket";
    public static final String IM = BASE_NAME + "im";
    public static final String ROUTER = BASE_NAME + "router";
    public static final String LOGIC = BASE_NAME + "logic";
}
