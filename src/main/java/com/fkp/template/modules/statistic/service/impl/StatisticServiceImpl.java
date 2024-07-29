package com.fkp.template.modules.statistic.service.impl;

import com.fkp.template.core.constant.CommonConstant;
import com.fkp.template.core.util.StrIdGenerator;
import com.fkp.template.modules.statistic.entity.StatisticCallCount;
import com.fkp.template.modules.statistic.service.StatisticService;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/7/23 16:12
 */
@Service
@Slf4j
public class StatisticServiceImpl implements StatisticService {

    @Resource(name = "callCountMapCache")
    private Cache<String, Map<String, StatisticCallCount>> callCountMapCache;

    @Autowired
    private StatisticCallCountManage statisticCallCountManage;

    @Override
    public void recordAkCallCount2Cache(String ak, boolean success){
        if(StringUtils.isNotBlank(ak)){
            Map<String, StatisticCallCount> callCountMap = callCountMapCache.getIfPresent(CommonConstant.CALL_COUNT_MAP_CACHE_NAME);
            if(callCountMap == null){
                log.error("recordAkCallCount2Cache error, callCountMap is null in cache. ak: {}, success: {}", ak, success);
                return;
            }
            callCountMap.compute(ak, (key, value) -> {
                StatisticCallCount statisticCallCount = value;
                if(statisticCallCount == null){
                    statisticCallCount = StatisticCallCount.instance(key, null);
                }
                if(success){
                    statisticCallCount.callSuccess();
                }else {
                    statisticCallCount.callFail();
                }
                return statisticCallCount;
            });
        }
    }

    @Override
    public void refreshAkCallCount2Database(){
        Map<String, StatisticCallCount> callCountMap = callCountMapCache.getIfPresent(CommonConstant.CALL_COUNT_MAP_CACHE_NAME);
        log.debug("refreshAkCallCount2Database callCountMap: {}", callCountMap);
        if(MapUtils.isEmpty(callCountMap)){
            return;
        }
        Map<String, StatisticCallCount> tmpMap = syncCopyProperties(callCountMap);
        if(tmpMap.size() > 0){
            List<String> errorList = saveCallCount(tmpMap);
            if(errorList.size() > 0){
                //错误需要将结果加到缓存中
                for (String ak : errorList) {
                    callCountMap.compute(ak, (key, value) -> {
                       StatisticCallCount statisticCallCount = value;
                        if(statisticCallCount == null){
                           statisticCallCount = StatisticCallCount.instance(ak, null);
                       }
                        statisticCallCount.change(tmpMap.get(ak));
                        return statisticCallCount;
                    });
                }
            }
        }
    }

    private Map<String, StatisticCallCount> syncCopyProperties(Map<String, StatisticCallCount> map){
        Map<String, StatisticCallCount> tmpMap = new HashMap<>();
        Set<String> keySet = map.keySet();
        for (String ak : keySet) {
            StatisticCallCount statisticCallCount = map.remove(ak);
            if(statisticCallCount != null){
                tmpMap.put(ak, statisticCallCount);
            }
        }
        return tmpMap;
    }

    private List<String> saveCallCount(Map<String, StatisticCallCount> params){
        List<String> errorAkList = new ArrayList<>();
        if(MapUtils.isNotEmpty(params)){
            for (Map.Entry<String, StatisticCallCount> entry : params.entrySet()) {
                String ak = entry.getKey();
                try {
                    Boolean dimensionExist = statisticCallCountManage.existDimension(ak, null);
                    StatisticCallCount statisticCallCount = entry.getValue();
                    statisticCallCount.setTotalNum(statisticCallCount.getSuccessNum() + statisticCallCount.getFailNum());
                    if(!dimensionExist){
                        try {
                            statisticCallCount.setId(StrIdGenerator.getInstance().nextId());
                            statisticCallCountManage.insert(statisticCallCount);
                        }catch (Exception e){
                            log.warn("refresh ak call count insert ak info to database error. try update count.", e);
                            statisticCallCountManage.accCallCountByDimension(ak, statisticCallCount.getSuccessNum(), statisticCallCount.getFailNum(), statisticCallCount.getTotalNum(), null);
                        }
                    }else {
                        statisticCallCountManage.accCallCountByDimension(ak, statisticCallCount.getSuccessNum(), statisticCallCount.getFailNum(), statisticCallCount.getTotalNum(), null);
                    }
                }catch (Exception e){
                    errorAkList.add(ak);
                    log.error("refresh ak call count update ak count to database error.", e);
                }
            }
        }
        return errorAkList;
    }
}
