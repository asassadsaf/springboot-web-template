package com.fkp.template.util;

import com.fkp.template.constant.*;
import com.fkp.template.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 业务参数校验工具类
 * @date 2024/5/21 14:44
 */
@Slf4j
public class ValidateUtils {
    private ValidateUtils(){}

    public static void wrapRandomLength(Integer keyLength, String wrappingAlgorithm){
        WrappingAlgorithmEnum wrappingAlgorithmEnum = WrappingAlgorithmEnum.of(wrappingAlgorithm);
        if(WrappingAlgorithmEnum.RSAES_OAEP_SHA1.equals(wrappingAlgorithmEnum)){
            if(keyLength < 1 || keyLength > 214){
                throw new BusinessException(ErrorCodeEnum.InvalidParameter, "Length", "The length should be between 1 and 214.");
            }
        }else if(WrappingAlgorithmEnum.RSAES_OAEP_SHA256.equals(wrappingAlgorithmEnum)){
            if(keyLength < 1 || keyLength > 190){
                throw new BusinessException(ErrorCodeEnum.InvalidParameter, "Length", "The length should be between 1 and 190.");
            }
        }
        if(keyLength < 1 || keyLength > 1024){
            throw new BusinessException(ErrorCodeEnum.InvalidParameter, "Length", "The length should be between 1 and 1024.");
        }
    }
}
