package com.fkp.template.modules.dbintegrity.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/18 20:11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyInfoVo {
    private String material;
    private String keyId;
    private String alg;
}
