package com.gmy.coder.chat.cache.config;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置
 *
 * @author gaomingyuan
 */
@Configuration
@EnableMethodCache(basePackages = "com.gmy.coder.chat")
public class CacheConfiguration {
}
