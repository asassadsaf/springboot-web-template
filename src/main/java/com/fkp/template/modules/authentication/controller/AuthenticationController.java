package com.fkp.template.modules.authentication.controller;

import com.fkp.template.core.constant.CommonConstant;
import com.fkp.template.core.dto.RestSimpleResponse;
import com.fkp.template.modules.authentication.params.request.LoginRequest;
import com.fkp.template.modules.authentication.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/7/24 19:11
 */
@RestController
@RequestMapping(value = CommonConstant.BASE_URL + "/" + CommonConstant.VERSION_V1 + "/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(value = "/login")
    public RestSimpleResponse<String> login(@RequestBody @Validated LoginRequest params){
        return authenticationService.login(params);
    }
}
