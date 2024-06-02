package com.fkp.template.constant;

import lombok.Getter;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/22 17:38
 */
@Getter
public enum PaddingModeEnum {

    PKCS7_PADDING("PKCS7PADDING"),
    NO_PADDING("NOPADDING");

    private final String value;

    PaddingModeEnum(String value) {
        this.value = value;
    }

    public static PaddingModeEnum of(String name){
        for (PaddingModeEnum value : PaddingModeEnum.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
