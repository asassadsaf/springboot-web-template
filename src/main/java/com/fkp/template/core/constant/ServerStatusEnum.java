package com.fkp.template.core.constant;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/14 16:45
 */
public enum ServerStatusEnum {
    // 正常服务中
    ACTIVE,
    // 可服务，但处于异常状态
    DEGRADED,
    // 不可服务状态
    UNAVAILABLE;
}
