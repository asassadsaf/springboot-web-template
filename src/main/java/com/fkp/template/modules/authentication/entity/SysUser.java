package com.fkp.template.modules.authentication.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/7/24 19:19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUser {
    private String id;
    private String username;
    private String password;
}
