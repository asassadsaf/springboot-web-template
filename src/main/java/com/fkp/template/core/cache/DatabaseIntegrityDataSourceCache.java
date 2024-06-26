package com.fkp.template.core.cache;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/20 11:19
 */
@Component
@Slf4j
public class DatabaseIntegrityDataSourceCache {

    @Resource(name = "dataSourceCache")
    private Cache<String, DataSource> cache;

    public void put(String url, DataSource dataSource){
        log.debug("put cache: url: {}, dataSource: {}", url, dataSource);
        cache.put(url, dataSource);
    }

    public DataSource getIfPresent(String url){
        log.debug("getIfPresent cache: url: {}", url);
        return cache.getIfPresent(url);
    }

    public void invalidate(String url){
        log.debug("invalidate cache: url: {}", url);
        cache.invalidate(url);
    }

}
