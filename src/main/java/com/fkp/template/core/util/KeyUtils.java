package com.fkp.template.core.util;

import com.fkp.template.core.constant.ErrorCodeEnum;
import com.fkp.template.core.exception.BusinessException;

import java.security.SecureRandom;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 单例模式生成随机数
 * @date 2024/6/2 16:15
 */
public class KeyUtils {
    private static final class SecureRandomHolder{
        public static SecureRandom SECURE_RANDOM;
        static {
            try {
                SECURE_RANDOM = new SecureRandom();
            } catch (Exception e) {
                throw new BusinessException(ErrorCodeEnum.InternalServerError, "SecureRandom get instance error.", e);
            }
        }
    }

    private KeyUtils() {
    }


    public static byte[] genRandom(int length){
        return SecureRandomHolder.SECURE_RANDOM.generateSeed(length);
    }
}

