package com.fkp.template.core.exception;

import com.fkp.template.core.constant.RestErrorEnum;
import lombok.Getter;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description Rest接口异常类
 * @date 2024/6/26 13:37
 */
@Getter
public class RestBusinessException extends RuntimeException{
    private final String code;

    public RestBusinessException(RestErrorEnum errorEnum) {
        super(errorEnum.getMsg());
        this.code = errorEnum.getCode();
    }

    public RestBusinessException(RestErrorEnum errorEnum, Throwable throwable) {
        super(errorEnum.getMsg(), throwable);
        this.code = errorEnum.getCode();
    }

    public RestBusinessException(String code, String messgae){
        super(messgae);
        this.code = code;
    }

    public RestBusinessException(String code, String messgae, Throwable throwable){
        super(messgae, throwable);
        this.code = code;
    }
}
