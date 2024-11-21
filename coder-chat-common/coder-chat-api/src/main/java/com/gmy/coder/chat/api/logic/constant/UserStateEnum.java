package com.gmy.coder.chat.api.logic.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户状态枚举
 *
 * @author gaomingyuan
 */
@AllArgsConstructor
@Getter
public enum UserStateEnum {
    NORMAL(1, "正常"),
    FROZEN(2, "冻结"),

    ;

    private final Integer code;
    private final String desc;

    private static final Map<Integer, UserStateEnum> cache;

    static {
        cache = Arrays.stream(UserStateEnum.values()).collect(Collectors.toMap(UserStateEnum::getCode, Function.identity()));
    }

    public static UserStateEnum of(Long type) {
        return cache.get(type);
    }
}
