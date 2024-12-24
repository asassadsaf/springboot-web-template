package com.fkp.template.modules.statistic.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/12/23 19:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KeyStateReport {
    private String name;
    private String id;
    private String alg;
    private Integer state;
}
