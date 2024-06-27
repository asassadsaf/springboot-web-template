package com.fkp.template.modules.dbintegrity.util;

import com.fkp.template.core.constant.RestErrorEnum;
import com.fkp.template.core.exception.RestBusinessException;
import com.fkp.template.core.util.SynthesisCryptoUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 密码加解密工具类
 * @date 2024/6/18 21:23
 */
@Slf4j
public class PwdUtils {
    private PwdUtils(){}

    public static String encPwd(String pwd){
        try {
            return SynthesisCryptoUtils.encPwd(pwd);
        }catch (Exception e){
            log.error("Encrypt password error.");
            throw new RestBusinessException(RestErrorEnum.CRYPTO_DEVICE_ERROR, e);
        }
    }

    public static String decPwd(String pwd){
        try {
            return SynthesisCryptoUtils.decPwd(pwd);
        } catch (Exception e) {
            log.error("Decrypt password error.");
            throw new RestBusinessException(RestErrorEnum.CRYPTO_DEVICE_ERROR, e);
        }
    }
}
