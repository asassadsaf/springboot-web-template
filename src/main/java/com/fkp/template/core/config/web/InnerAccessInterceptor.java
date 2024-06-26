package com.fkp.template.core.config.web;

import com.fkp.template.core.constant.ErrorCodeEnum;
import com.fkp.template.core.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description spring web拦截器-只允许环回地址访问
 * @date 2024/5/29 17:29
 */

@Slf4j
@Component
public class InnerAccessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String remoteHost = request.getRemoteHost();
        Exception exception = null;
        try {
            if(StringUtils.isNotBlank(remoteHost) && InetAddress.getByName(request.getRemoteHost()).isLoopbackAddress()){
                return true;
            }
        }catch (Exception e){
            exception = e;
        }
        log.error("Inner api is not allow access. remoteHost: {}", remoteHost, exception);
        throw new BusinessException(ErrorCodeEnum.InvalidRequest, "Inner api is not allow access.");
    }
}
