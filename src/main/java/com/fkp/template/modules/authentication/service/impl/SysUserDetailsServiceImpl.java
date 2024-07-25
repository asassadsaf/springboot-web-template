package com.fkp.template.modules.authentication.service.impl;

import com.fkp.template.core.constant.RestErrorEnum;
import com.fkp.template.core.exception.RestBusinessException;
import com.fkp.template.modules.authentication.entity.SysUser;
import com.fkp.template.modules.authentication.entity.SysUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/7/24 19:30
 */
@Service(value = "sysUserDetailsServiceImpl")
public class SysUserDetailsServiceImpl implements UserDetailsService {

    @Value("${authentication.username:fkp}")
    private String username;

    @Value("${authentication.password:123456}")
    private String password;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //通过用户名查询UserDetails
        if(!this.username.equals(username)){
            throw new RestBusinessException(RestErrorEnum.AUTHENTICATION_ERROR);
        }
        SysUser sysUser = SysUser.builder().id("1").username(username).password(password).build();
        return SysUserDetails.builder().sysUser(sysUser).build();
    }

}
