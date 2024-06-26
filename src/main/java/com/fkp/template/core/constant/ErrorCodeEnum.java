package com.fkp.template.core.constant;

import lombok.Getter;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/13 16:39
 */
@Getter
public enum ErrorCodeEnum {

    InvalidRequest(400, "InvalidRequest"),
    InvalidSignature(400, "InvalidSignature"),
    InvalidOperation(400, "InvalidOperation"),
    ParameterRequired(400, "%sRequired"),
    InvalidParameter(400, "Invalid%s"),
    RequestMetadataRequired(400, "RequestMetadataRequired"),
    InvalidRequestMetadata(400, "InvalidRequestMetadata"),
    InvalidAcsDate(400, "InvalidAcsDate"),
    Unauthorized(401, "Unauthorized"),
    KeyDisabledForbidden(403, "KeyDisabledForbidden"),
    KeyPendingDeletionForbidden(400, "KeyPendingDeletionForbidden"),
    KeyUsageForbidden(403, "KeyUsageForbidden"),
    InvalidKeyState(400, "InvalidKeyState"),
    AccessForbidden(403, "AccessForbidden"),
    XkiNotFound(404, "XkiNotFound"),
    KeyNotFound(404, "KeyNotFound"),
    MethodNotAllowed(405, "MethodNotAllowed"),
    TooManyRequests(429, "TooManyRequests"),
    InternalServerError(500, "InternalServerError"),
    ServiceUnavaiable(503, "ServiceUnavaiable"),
    Timeout(504, "Timeout");


    private final Integer statusCode;
    private final String errorCode;

    ErrorCodeEnum(Integer statusCode, String errorCode) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }
}
