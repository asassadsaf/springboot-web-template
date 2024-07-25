package com.fkp.template.modules.authentication.service;

import com.fkp.template.core.dto.RestSimpleResponse;
import com.fkp.template.modules.authentication.params.request.LoginRequest;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/7/24 19:18
 */
public interface AuthenticationService {
    RestSimpleResponse<String> login(LoginRequest params);
}
