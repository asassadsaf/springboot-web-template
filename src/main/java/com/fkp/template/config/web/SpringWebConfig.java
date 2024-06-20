package com.fkp.template.config.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description spring web拦截器配置—用于注册过滤器
 * @date 2024/5/13 16:09
 */
@Configuration(proxyBeanMethods = false)
public class SpringWebConfig implements WebMvcConfigurer {

    @Autowired
    private MyInterceptor myInterceptor;

    @Autowired
    private InnerAccessInterceptor innerAccessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(myInterceptor).addPathPatterns("/**")
//                .excludePathPatterns(CommonConstant.BASE_URL + "/" + CommonConstant.VERSION_V1 + "/system/**")
//                .excludePathPatterns("/error");
//        registry.addInterceptor(innerAccessInterceptor).addPathPatterns(CommonConstant.BASE_URL + "/" + CommonConstant.VERSION_V1 + "/system/**");
    }
}
