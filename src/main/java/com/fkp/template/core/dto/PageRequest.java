package com.fkp.template.core.dto;

import lombok.Data;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description Rest接口分页请求基类
 * @date 2024/6/26 14:01
 */
@Data
public class PageRequest {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Boolean asc = false;
}
