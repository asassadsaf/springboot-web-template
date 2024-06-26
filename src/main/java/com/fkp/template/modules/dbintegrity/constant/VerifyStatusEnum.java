package com.fkp.template.modules.dbintegrity.constant;

import lombok.Getter;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/18 19:59
 */
@Getter
public enum VerifyStatusEnum {
    NOT_VERIFY(0),
    SUCCESS(1),
    FAIL(2),
    ERROR(3);


    private final Integer value;

    VerifyStatusEnum(Integer value) {
        this.value = value;
    }
}
