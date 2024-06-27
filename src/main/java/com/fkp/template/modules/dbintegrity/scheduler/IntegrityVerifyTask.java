package com.fkp.template.modules.dbintegrity.scheduler;

import com.fkp.template.modules.dbintegrity.service.DatabaseIntegrityService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 校验数据库完整性任务类，注册为原型模式的Bean,使用Spring声明式事务
 * @date 2024/6/20 10:32
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class IntegrityVerifyTask implements Runnable{
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private Integer frequency;

    @Setter
    private ConcurrentMap<String, ScheduledFuture<?>> schedulerMap;

    @Autowired
    private DatabaseIntegrityService service;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run() {
        log.debug("Verify integrity scheduler exec task start...id: {}, frequency: {}", id, frequency);
        try {
            service.verify(id);
        }catch (Exception e){
            log.error("Verify integrity scheduler exec task exception. id: {}, frequency: {}", id, frequency, e);
            Optional.ofNullable(schedulerMap.remove(id)).ifPresent(future -> future.cancel(false));
        }
    }
}
