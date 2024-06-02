package com.fkp.template.constant;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/21 14:55
 */
public enum OutputFormEnum {
    NONE,
    PLAIN,
    PUBLIC_KEY_WRAPPED;


    public static OutputFormEnum of(String name){
        for (OutputFormEnum value : OutputFormEnum.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
