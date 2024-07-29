package com.fkp.template.modules.statistic.service;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/7/23 16:12
 */
public interface StatisticService {
    void recordAkCallCount2Cache(String ak, boolean success);

    void refreshAkCallCount2Database();

}
