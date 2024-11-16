package com.gmy.coder.chat.api.websocket.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ws前端请求类型枚举
 *
 * @author gaomingyuan
 */
@AllArgsConstructor
@Getter
public enum WSReqTypeEnum {
    HEARTBEAT(0, "心跳包"),
    MESSAGE(1,"新消息")
    ;

    private final Integer type;
    private final String desc;

    private static Map<Integer, WSReqTypeEnum> cache;

    static {
        cache = Arrays.stream(WSReqTypeEnum.values()).collect(Collectors.toMap(WSReqTypeEnum::getType, Function.identity()));
    }

    public static WSReqTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
