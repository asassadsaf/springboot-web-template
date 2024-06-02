package com.fkp.template.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/29 19:11
 */
public class LogUtils {
    private static final Logger LOG = LoggerFactory.getLogger("operationLog");

    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    public static void printLog(boolean success, String oper, String requestId, String kmsRequestId, String principalArn,
                                String kmsInstanceId, String kmsOperation, String keyId, String errorCode, String errorMsg){
        StringBuilder sb = new StringBuilder();
        sb.append("result: [{}], operation: [{}], request id: [{}], kms request id: [{}], principal arn: [{}], kms instance id: [{}], kms operation: [{}], keyId: [{}]");
        if(!success){
            sb.append(", error code: [{}], error message: [{}]");
        }
        LOG.info(sb.toString(), success ? SUCCESS : FAIL, oper, requestId, kmsRequestId, Optional.ofNullable(principalArn).orElse(StringUtils.EMPTY),
                Optional.ofNullable(kmsInstanceId).orElse(StringUtils.EMPTY), Optional.ofNullable(kmsOperation).orElse(StringUtils.EMPTY), keyId, errorCode, errorMsg);
    }
}
