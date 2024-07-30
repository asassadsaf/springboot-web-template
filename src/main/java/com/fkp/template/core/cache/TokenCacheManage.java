package com.fkp.template.core.cache;

import com.fkp.template.modules.authentication.entity.SysUserDetails;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/7/25 13:58
 */
@Component
public class TokenCacheManage {
//    @Resource(name = "tokenCache")
//    private Cache<String, SysUserDetails> tokenCache;

    @Cacheable(cacheNames = "tokenCache", key = "#tokenKey", cacheManager = "tokenCaffeineCacheManager")
    public SysUserDetails getIfPresent(String tokenKey){
        return null;
    }

    @CachePut(cacheNames = "tokenCache", key = "#tokenKey", cacheManager = "tokenCaffeineCacheManager")
    public SysUserDetails put(String tokenKey, SysUserDetails sysUserDetails){
        return sysUserDetails;
    }

    @CacheEvict(cacheNames = "tokenCache", key = "#tokenKey", cacheManager = "tokenCaffeineCacheManager")
    public void invalidate(String tokenKey){
    }
}
