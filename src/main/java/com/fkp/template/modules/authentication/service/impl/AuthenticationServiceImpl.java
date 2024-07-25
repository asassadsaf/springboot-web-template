package com.fkp.template.modules.authentication.service.impl;

import com.fkp.template.core.constant.RestErrorEnum;
import com.fkp.template.core.dto.RestSimpleResponse;
import com.fkp.template.core.exception.RestBusinessException;
import com.fkp.template.modules.authentication.entity.SysUserDetails;
import com.fkp.template.modules.authentication.params.request.LoginRequest;
import com.fkp.template.modules.authentication.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/7/24 19:18
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Override
    public RestSimpleResponse<String> login(LoginRequest params) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(params.getUsername(), params.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(authenticate == null){
            throw new RestBusinessException(RestErrorEnum.AUTHENTICATION_ERROR);
        }
        SysUserDetails sysUserDetails = (SysUserDetails) authenticate.getPrincipal();
        String token = tokenService.createToken(sysUserDetails);
        return RestSimpleResponse.success(token);
    }
}
