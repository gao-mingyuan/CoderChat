package com.gmy.coder.chat.api.router.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 路由socket请求类型枚举
 *
 * @author gaomingyuan
 */
@AllArgsConstructor
@Getter
public enum RouterReqTypeEnum {
    HEARTBEAT(0, "心跳包"),
    REGISTER(1, "服务注册"),
    ;

    private final Integer type;
    private final String desc;

    private static Map<Integer, RouterReqTypeEnum> cache;

    static {
        cache = Arrays.stream(RouterReqTypeEnum.values()).collect(Collectors.toMap(RouterReqTypeEnum::getType, Function.identity()));
    }

    public static RouterReqTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
