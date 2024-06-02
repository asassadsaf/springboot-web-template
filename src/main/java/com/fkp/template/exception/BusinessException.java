package com.fkp.template.exception;

import com.fkp.template.constant.ErrorCodeEnum;
import lombok.Getter;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 自定义业务异常
 * @date 2024/5/13 16:50
 */
@Getter
public class BusinessException extends RuntimeException{
    private final Integer statusCode;
    private final String errorCode;

    public BusinessException(ErrorCodeEnum errorCodeEnum, String message) {
        super(message);
        this.statusCode = errorCodeEnum.getStatusCode();
        this.errorCode = errorCodeEnum.getErrorCode();
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum, String replace, String message) {
        super(message);
        this.statusCode = errorCodeEnum.getStatusCode();
        this.errorCode = String.format(errorCodeEnum.getErrorCode(), replace);
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = errorCodeEnum.getStatusCode();
        this.errorCode = errorCodeEnum.getErrorCode();
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum, String replace, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = errorCodeEnum.getStatusCode();
        this.errorCode = String.format(errorCodeEnum.getErrorCode(), replace);
    }
}
