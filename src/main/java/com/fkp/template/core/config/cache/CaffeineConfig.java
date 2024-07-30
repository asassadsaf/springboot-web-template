package com.fkp.template.core.config.cache;

import com.alibaba.druid.pool.DruidDataSource;
import com.fkp.template.core.constant.CommonConstant;
import com.fkp.template.modules.authentication.entity.SysUserDetails;
import com.fkp.template.modules.statistic.entity.StatisticCallCount;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 初始化Caffeine Cache
 * @date 2024/5/28 17:52
 */
@Configuration(proxyBeanMethods = false)
@Slf4j
public class CaffeineConfig {

    @Value("${business.databaseIntegrity.dataSourceCache.maxSize:128}")
    private Long maxSize;

    @Value("${business.databaseIntegrity.dataSourceCache.expireAfterAccessMinute:1440}")
    private Long expireAfterAccessMinute;

    // 令牌有效期（默认30分钟）
    @Value("${authentication.token.expireTime:30}")
    private int tokenExpireTime;

    @Bean(name = "dataSourceCache")
    public Cache<String, DataSource> dataSourceCache(){
        return Caffeine.newBuilder()
                .initialCapacity(8)//初始大小
                .maximumSize(maxSize)//最大数量
                // 最后一次访问后多长时间过期
                .expireAfterAccess(expireAfterAccessMinute, TimeUnit.MINUTES)
                // 当元素从缓存冲逐出后执行的监听器，当操作缓存时才触发
                .removalListener((key, value, cause) -> {
                    if(value instanceof DruidDataSource){
                        log.debug("removalListener dataSource: {}", value);
                        ((DruidDataSource)value).close();
                    }
                })
                .build();
    }

    @Bean(name = "policyFileCache")
    public Cache<String, Object> policyFileCache() {
        return Caffeine.newBuilder()
                .initialCapacity(8)//初始大小
                .maximumSize(32)//最大数量
                .build();
    }

    @Bean(name = "tokenCacheCaffeine")
    public Caffeine<Object, Object> tokenCacheCaffeine() {
        return Caffeine.newBuilder()
                .initialCapacity(8)//初始大小
                .maximumSize(32)//最大数量
                .expireAfterWrite(tokenExpireTime, TimeUnit.MINUTES);
    }

    @Bean(name = "callCountMapCache")
    public Cache<String, Map<String, StatisticCallCount>> callCountMapCache() {
        Cache<String, Map<String, StatisticCallCount>> cache = Caffeine.newBuilder()
                // 初始的缓存空间大小
                .initialCapacity(1)
                // 缓存的最大条数
                .maximumSize(1)
                .build();
        cache.put(CommonConstant.CALL_COUNT_MAP_CACHE_NAME, new ConcurrentHashMap<>(16));
        return cache;
    }
}
