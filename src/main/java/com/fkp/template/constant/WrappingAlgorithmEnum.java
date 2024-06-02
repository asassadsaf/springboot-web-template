package com.fkp.template.constant;

import lombok.Getter;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/21 15:08
 */
@Getter
public enum WrappingAlgorithmEnum {
    SM2PKE("SM2"),
    RSAES_OAEP_SHA1("RSA/NONE/OAEPWITHSHA1ANDMGF1PADDING"),
    RSAES_OAEP_SHA256("RSA/NONE/OAEPWITHSHA256ANDMGF1PADDING");

    private final String value;

    WrappingAlgorithmEnum(String value) {
        this.value = value;
    }

    public static WrappingAlgorithmEnum of(String name){
        for (WrappingAlgorithmEnum value : WrappingAlgorithmEnum.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
