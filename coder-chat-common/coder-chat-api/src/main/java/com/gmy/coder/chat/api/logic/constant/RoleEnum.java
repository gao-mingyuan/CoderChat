package com.gmy.coder.chat.api.logic.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 *
 * @author gaomingyuan
 */
@AllArgsConstructor
@Getter
public enum RoleEnum {
    CUSTOMER(1, "普通用户"),
    ADMIN(2, "超级管理员"),
    ;

    private final Integer code;
    private final String desc;

    private static final Map<Integer, RoleEnum> cache;

    static {
        cache = Arrays.stream(RoleEnum.values()).collect(Collectors.toMap(RoleEnum::getCode, Function.identity()));
    }

    public static RoleEnum of(Long type) {
        return cache.get(type);
    }
}
