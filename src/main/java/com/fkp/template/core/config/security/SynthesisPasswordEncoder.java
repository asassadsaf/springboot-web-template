package com.fkp.template.core.config.security;

import com.fkp.template.core.constant.RestErrorEnum;
import com.fkp.template.core.exception.RestBusinessException;
import com.fkp.template.core.util.SynthesisCryptoUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.CryptoException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/7/25 16:14
 */
@Component
public class SynthesisPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        try {
            return SynthesisCryptoUtils.encPwd(rawPassword.toString());
        }catch (Exception e){
            throw new RestBusinessException(RestErrorEnum.CRYPTO_DEVICE_ERROR, e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            String decPwd = SynthesisCryptoUtils.decPwd(encodedPassword);
            return StringUtils.equals(rawPassword, decPwd);
        }catch (Exception e){
            throw new RestBusinessException(RestErrorEnum.CRYPTO_DEVICE_ERROR, e);
        }
    }
}
