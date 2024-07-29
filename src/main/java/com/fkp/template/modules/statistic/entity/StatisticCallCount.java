package com.fkp.template.modules.statistic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 *
 * @author fengkunpeng
 * @since 2024-07-22
 */
@Data
@TableName("statistic_call_count")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticCallCount implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String dimension;

    private Long successNum;

    private Long failNum;

    private Long totalNum;

    private String tenantAccount;


    public static StatisticCallCount instance(String dimension, String tenantAccount){
        return StatisticCallCount.builder().dimension(dimension).successNum(0L).failNum(0L).totalNum(0L)
                .tenantAccount(StringUtils.defaultIfBlank(tenantAccount, "SYSTEMMANAGE")).build();
    }

    public void callSuccess(){
        this.successNum += 1;
    }

    public void callFail(){
        this.failNum += 1;
    }

    public void change(StatisticCallCount statisticCallCount){
        this.successNum += statisticCallCount.getSuccessNum();
        this.failNum += statisticCallCount.getFailNum();
    }


}
