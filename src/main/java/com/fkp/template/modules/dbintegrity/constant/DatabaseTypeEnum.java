package com.fkp.template.modules.dbintegrity.constant;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/18 11:04
 */
public enum DatabaseTypeEnum {
    MYSQL,
    DM;

    public static DatabaseTypeEnum of(String name){
        for (DatabaseTypeEnum value : DatabaseTypeEnum.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
