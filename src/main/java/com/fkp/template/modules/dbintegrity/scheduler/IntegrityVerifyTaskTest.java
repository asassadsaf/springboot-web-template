package com.fkp.template.modules.dbintegrity.scheduler;

import com.fkp.template.modules.dbintegrity.service.DatabaseIntegrityService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 演示使用new Runnable方式的匿名内部类创建的任务，如何手动提交事务
 * @date 2024/6/20 10:32
 */

@Slf4j
public class IntegrityVerifyTaskTest implements Runnable{
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private Integer frequency;

    @Setter
    private ConcurrentMap<String, ScheduledFuture<?>> schedulerMap;

    @Setter
    private DatabaseIntegrityService service;

    @Setter
    private PlatformTransactionManager transactionManager;

    @Setter
    private TransactionDefinition transactionDefinition;

    @Override
    public void run() {
        log.debug("Verify integrity scheduler exec task start...id: {}, frequency: {}", id, frequency);
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        try {
            service.verify(id);
            //成功提交事务
            transactionManager.commit(transaction);
        }catch (Exception e){
            log.error("Verify integrity scheduler exec task exception. id: {}, frequency: {}", id, frequency, e);
            //失败回滚事务
            transactionManager.rollback(transaction);
            Optional.ofNullable(schedulerMap.remove(id)).ifPresent(future -> future.cancel(false));
        }
    }
}
