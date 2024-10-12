package com.fkp.template.modules.dbintegrity.entity;

import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/10/12 15:56
 */
@Component
@ToString(callSuper = true)
public class ChildTest extends ParentTest{


    public void setServerName(String serverName){
        this.serverName = serverName;
    }

    @Value("${business.serverVersion}")
    public void setServerVersion(String serverVersion){
        this.serverVersion = serverVersion;
    }
}
