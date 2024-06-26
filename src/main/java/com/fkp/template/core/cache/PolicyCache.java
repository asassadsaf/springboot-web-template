package com.fkp.template.core.cache;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 策略缓存入口
 * @date 2024/5/28 18:00
 */
@Component
public class PolicyCache {

    @Resource(name = "policyFileCache")
    private Cache<String, Object> caffeine;

    public void put(String key, Object value){
        caffeine.put(key, value);
    }

    public Object get(String key){
        return caffeine.getIfPresent(key);
    }

}
