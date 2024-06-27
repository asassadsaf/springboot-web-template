package com.fkp.template.modules.dbintegrity.constant;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 数据库完整性支持的数据库类型枚举
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
