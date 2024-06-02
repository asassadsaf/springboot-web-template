package com.fkp.template.constant;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/27 14:51
 */
public enum ConfigIpWhiteListOperEnum {
    QUERY,
    ADD,
    DEL;

    public static ConfigIpWhiteListOperEnum of(String name){
        for (ConfigIpWhiteListOperEnum value : ConfigIpWhiteListOperEnum.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }


}
