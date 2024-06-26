package com.fkp.template.core.constant;

import lombok.Getter;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/22 16:30
 */
@Getter
public enum AlgorithmModeEnum {

    AES_ECB(AlgorithmEnum.AES, ModeEnum.ECB),
    AES_CBC(AlgorithmEnum.AES, ModeEnum.CBC),
    AES_GCM(AlgorithmEnum.AES, ModeEnum.GCM),
    SM4_GCM(AlgorithmEnum.SM4, ModeEnum.GCM);


    private final AlgorithmEnum alg;
    private final ModeEnum mode;

    AlgorithmModeEnum(AlgorithmEnum alg, ModeEnum mode) {
        this.alg = alg;
        this.mode = mode;
    }

    public static AlgorithmModeEnum of(String name){
        for (AlgorithmModeEnum value : AlgorithmModeEnum.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
