package com.fkp.template.aop;

import com.fkp.template.dto.request.RequestMetadata;
import com.fkp.template.dto.response.BaseResponse;
import com.fkp.template.util.LogUtils;
import com.fkp.template.dto.request.BaseRequest;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 使用切面记录成功的操作日志
 * @date 2024/5/29 17:00
 */
@Aspect
@Component
@Order
public class LogAspect {

    //定义切点，只记录某个控制器下的接口
    @Pointcut("execution(* com.fkp.template.controller.TestController.*(..))")
    public void operationLogPointcut() {}

    @Around("operationLogPointcut()")
    public Object handle(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object proceed = joinPoint.proceed(args);
        //正常执行没有发生异常
        String oper = joinPoint.getSignature().getName();
        String kmsRequestId = StringUtils.EMPTY;
        String kmsInstanceId = StringUtils.EMPTY;
        String kmsOperation = StringUtils.EMPTY;
        String principalArn = StringUtils.EMPTY;
        String keyId = StringUtils.EMPTY;
        String requestId = StringUtils.EMPTY;
        if(ArrayUtils.isNotEmpty(args)){
            for (Object arg : args) {
                if(arg instanceof BaseRequest){
                    BaseRequest request = (BaseRequest)arg;
                    RequestMetadata requestMetadata = request.getRequestMetadata();
                    if(requestMetadata != null){
                        kmsRequestId = requestMetadata.getKmsRequestId();
                        kmsInstanceId = requestMetadata.getKmsInstanceId();
                        kmsOperation = requestMetadata.getKmsOperation();
                        principalArn = requestMetadata.getPrincipalArn();
                    }
                }else if(arg instanceof String && StringUtils.isNotBlank((String) arg) && ((String) arg).length() == 40){
                    keyId = (String) arg;
                }
            }
        }
        if(proceed instanceof BaseResponse){
            BaseResponse response = (BaseResponse) proceed;
            requestId = response.getRequestId();
        }
        LogUtils.printLog(true, oper, requestId, kmsRequestId, principalArn, kmsInstanceId, kmsOperation, keyId, null, null);
        return proceed;
    }
}
