package com.fkp.template.modules.dbintegrity.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/19 13:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseQueryVo {
    private String url;
    private String driverClassName;
    private String username;
    private String password;
    private String execSql;
}
