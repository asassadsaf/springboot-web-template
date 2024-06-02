package com.fkp.template.constant;

import lombok.Getter;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/21 15:09
 */
@Getter
public enum WrappingKeySpecEnum {
    EC_SM2("SM2"),
    RSA_2048("RSA");

    private final String value;

    WrappingKeySpecEnum(String value) {
        this.value = value;
    }

    public static WrappingKeySpecEnum of(String name){
        for (WrappingKeySpecEnum value : WrappingKeySpecEnum.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
