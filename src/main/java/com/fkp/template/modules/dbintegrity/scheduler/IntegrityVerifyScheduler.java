package com.fkp.template.modules.dbintegrity.scheduler;

import com.fkp.template.modules.dbintegrity.entity.DatabaseIntegrity;
import com.fkp.template.modules.dbintegrity.service.DatabaseIntegrityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/19 19:58
 */
@Component
@Slf4j
public class IntegrityVerifyScheduler {

    @Autowired
    private DatabaseIntegrityService databaseIntegrityService;

    @Scheduled(fixedRateString = "${business.databaseIntegrity.verifySchedulingRate:10}", timeUnit = TimeUnit.MINUTES)
    private void queryAndRegisterScheduler(){
        for (DatabaseIntegrity databaseIntegrity : databaseIntegrityService.listIdAndFrequency()) {
            databaseIntegrityService.startVerifyIntegrityScheduler(databaseIntegrity.getId(), databaseIntegrity.getFrequency());
        }
    }
}
