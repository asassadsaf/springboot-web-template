package com.fkp.template.modules.dbintegrity.entity;

import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/10/12 15:55
 */
@ToString
public abstract class ParentTest {
    @Value("${business.serverName}")
    protected String serverName;
    protected String serverVersion;
}
