package com.fkp.template.modules.authentication.params.request;

import com.fkp.template.core.validator.order.OrderSequence;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/7/24 19:15
 */
@Data
public class LoginRequest {
    @NotBlank(message = "username is not blank.", groups = OrderSequence.OrderA.class)
    @Size(max = 32, message = "The username cannot exceed 32 characters in length.", groups = OrderSequence.OrderA.class)
    private String username;
    @NotBlank(message = "password is not blank.", groups = OrderSequence.OrderB.class)
    @Size(max = 32, message = "The password cannot exceed 32 characters in length.", groups = OrderSequence.OrderB.class)
    private String password;
}
