package com.fkp.template.modules.statistic.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fkp.template.core.constant.CommonConstant;
import com.fkp.template.core.constant.RestErrorEnum;
import com.fkp.template.core.exception.RestBusinessException;
import com.fkp.template.core.util.MybatisPlusUtils;
import com.fkp.template.modules.statistic.entity.StatisticCallCount;
import com.fkp.template.modules.statistic.mapper.StatisticCallCountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/7/23 10:52
 */
@Service
public class StatisticCallCountManage {

    @Autowired
    private StatisticCallCountMapper statisticCallCountMapper;

    public Boolean existDimension(String dimension, String tenantAccount){
        Long count = statisticCallCountMapper.selectCount(Wrappers.lambdaQuery(StatisticCallCount.class)
                .eq(StatisticCallCount::getDimension, dimension)
                .eq(StatisticCallCount::getTenantAccount, Optional.ofNullable(tenantAccount).orElse(CommonConstant.DEFAULT_TENANTACCOUNT)));
        if(count > 1){
            throw new RestBusinessException(RestErrorEnum.PARAMS_INVALID.getCode(), "select dimension count from db, result more than 1. dimension: {}, tenantAccount: {}" +  dimension + tenantAccount);
        }
        return count == 1;
    }

    public void insert(StatisticCallCount statisticCallCount){
        int insert = statisticCallCountMapper.insert(statisticCallCount);
        if(insert == 0){
            throw new RestBusinessException(RestErrorEnum.DATABASE_EXCEPTION);
        }
    }

    public void accCallCountByDimension(String dimension, Long successNum, Long failNum, Long totalNum, String tenantAccount){
        String successNumColName = MybatisPlusUtils.getColumnNameByRef(StatisticCallCount::getSuccessNum);
        String failNumColName = MybatisPlusUtils.getColumnNameByRef(StatisticCallCount::getFailNum);
        String totalNumColName = MybatisPlusUtils.getColumnNameByRef(StatisticCallCount::getTotalNum);
        int update = statisticCallCountMapper.update(Wrappers.lambdaUpdate(StatisticCallCount.class)
                .setSql(successNum != 0L, successNumColName + " = " + successNumColName + " + " + successNum)
                .setSql(failNum != 0L, failNumColName + " = " + failNumColName + " + " + failNum)
                .setSql(totalNum != 0L, totalNumColName + " = " + totalNumColName + " + " + totalNum)
                .eq(StatisticCallCount::getDimension, dimension)
                .eq(StatisticCallCount::getTenantAccount, Optional.ofNullable(tenantAccount).orElse(CommonConstant.DEFAULT_TENANTACCOUNT)));
        if(update == 0){
            throw new RestBusinessException(RestErrorEnum.DATABASE_EXCEPTION);
        }
    }

    public List<StatisticCallCount> selectList(){
        return statisticCallCountMapper.selectList(Wrappers.lambdaQuery(StatisticCallCount.class)
                .eq(StatisticCallCount::getTenantAccount, CommonConstant.DEFAULT_TENANTACCOUNT));
    }
}
