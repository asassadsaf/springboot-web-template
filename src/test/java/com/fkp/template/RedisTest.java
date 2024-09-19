package com.fkp.template;

import com.fkp.template.core.util.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/8/28 16:39
 */
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisUtils redisUtils;

    @Test
    void test(){
        //存储字符串k v
        redisUtils.set("name", "fkp");
        // 根据k获取字符串v
        Object value = redisUtils.get("name");
        System.out.println(value);
        // 给指定k设置过期时间
        boolean expireTime = redisUtils.expire("name", 5);
        System.out.println(expireTime);
        // 获取指定k的过期时间
        long expireTimeValue = redisUtils.getTime("name");
        System.out.println(expireTimeValue);
        // 判断是否存在k的缓存
        boolean hasKeyValue = redisUtils.hasKey("name");
        System.out.println(hasKeyValue);
        // 移除指定k的过期时间
        boolean persistValue = redisUtils.persist("name");
        System.out.println(persistValue);
        long persistAfterTimeValue = redisUtils.getTime("name");
        System.out.println(persistAfterTimeValue);
    }
}
