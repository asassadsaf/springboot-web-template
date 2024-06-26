package com.fkp.template.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description Rest接口分页响应类
 * @date 2024/6/26 13:51
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO<T> implements Serializable {
    private static final long serialVersionUID = -1078779649472488950L;
    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private List<T> list;
}
