package com.fkp.template.modules.statistic.scheduler;

import com.fkp.template.modules.statistic.service.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/7/23 16:37
 */
@Component
@Slf4j
public class StatisticScheduler {

    @Autowired
    private StatisticService statisticService;

    @Scheduled(cron = "${business.statistic.callCountCron:0/10 * * * * ?}")
    private void refreshAkCallCount2DatabaseScheduler(){
        statisticService.refreshAkCallCount2Database();
    }


}
