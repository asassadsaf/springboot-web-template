package com.fkp.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/10/10 19:09
 */
@SpringBootTest
@Slf4j
public class TaskTest {

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Test
    void taskSchedulerTest(){
        int poolSize = taskScheduler.getPoolSize();
        int activeCount = taskScheduler.getActiveCount();
        log.info("poolSize: {}", poolSize);
        log.info("activeCount: {}", activeCount);
        // 使用和@Scheduled注解类似

    }

    @Test
    void taskExecutorTest(){
        int corePoolSize = taskExecutor.getCorePoolSize();
        int activeCount = taskExecutor.getActiveCount();
        int maxPoolSize = taskExecutor.getMaxPoolSize();
        int queueCapacity = taskExecutor.getQueueCapacity();
        int queueSize = taskExecutor.getQueueSize();
        int keepAliveSeconds = taskExecutor.getKeepAliveSeconds();
        log.info("corePoolSize: {}", corePoolSize);
        log.info("activeCount: {}", activeCount);
        log.info("maxPoolSize: {}", maxPoolSize);
        log.info("queueCapacity: {}", queueCapacity);
        log.info("queueSize: {}", queueSize);
        log.info("keepAliveSeconds: {}", keepAliveSeconds);
    }


}
