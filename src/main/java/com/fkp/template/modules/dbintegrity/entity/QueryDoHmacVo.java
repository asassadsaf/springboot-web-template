package com.fkp.template.modules.dbintegrity.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/18 17:59
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryDoHmacVo {
    private String hmacHex;
    private Long rowCount;
}
