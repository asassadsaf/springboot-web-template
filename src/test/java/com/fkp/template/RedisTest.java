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
        redisUtils.set("name", "fkp");
        Object value = redisUtils.get("name");
        System.out.println(value);
    }
}
