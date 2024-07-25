package com.fkp.template.core.cache;

import com.fkp.template.modules.authentication.entity.SysUserDetails;
import com.github.benmanes.caffeine.cache.Cache;
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
    @Resource(name = "tokenCache")
    private Cache<String, SysUserDetails> tokenCache;

    public SysUserDetails getIfPresent(String tokenKey){
        return tokenCache.getIfPresent(tokenKey);
    }

    public void put(String tokenKey, SysUserDetails sysUserDetails){
        tokenCache.put(tokenKey, sysUserDetails);
    }

    public void invalidate(String tokenKey){
        tokenCache.invalidate(tokenKey);
    }
}
