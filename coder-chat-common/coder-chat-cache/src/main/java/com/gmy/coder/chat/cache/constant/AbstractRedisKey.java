package com.gmy.coder.chat.cache.constant;

import jakarta.annotation.PostConstruct;

/**
 * redisKey抽象类
 *
 * @author gaomingyuan
 */
public abstract class AbstractRedisKey {
    private static String BASE_KEY;

    @PostConstruct
    private void setBaseKey() {
        BASE_KEY = getBaseKey();
    }

    abstract String getBaseKey();

    public static String getKey(String key, Object... objects) {
        return BASE_KEY + String.format(key, objects);
    }

}
