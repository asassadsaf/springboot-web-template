package com.fkp.template.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 初始化一个Caffeine Cache
 * @date 2024/5/28 17:52
 */
@Configuration(proxyBeanMethods = false)
public class CaffeineConfig {

    @Bean
    public Cache<String, Object> policyFileCache() {
        return Caffeine.newBuilder()
                .initialCapacity(8)//初始大小
                .maximumSize(32)//最大数量
                .build();
    }
}
