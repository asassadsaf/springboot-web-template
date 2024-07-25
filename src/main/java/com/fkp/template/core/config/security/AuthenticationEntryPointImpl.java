package com.fkp.template.core.config.security;

import com.alibaba.fastjson2.JSON;
import com.fkp.template.core.constant.RestErrorEnum;
import com.fkp.template.core.dto.RestSimpleResponse;
import com.fkp.template.core.exception.RestBusinessException;
import com.fkp.template.core.util.ServletUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 * 
 *
 * @author fengkunpeng
 */
@Component(value = "authenticationEntryPointImpl")
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        RestSimpleResponse<Object> data = RestSimpleResponse.fail(RestErrorEnum.AUTHORIZATION_ERROR.getCode(), e.getMessage());
        ServletUtils.renderString(response, data);
    }
}
