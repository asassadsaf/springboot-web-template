package com.fkp.template.config;

import com.fkp.template.entity.CertDigestBean;
import com.fkp.template.entity.CertDigestListBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 配置类，用于注册Bean
 * @date 2024/6/13 9:48
 */
@Configuration(proxyBeanMethods = false)
public class BeanRegisterConfiguration {

    //   通过@Bean注册           通过手动注册      通过@Service注册
    // certDigestListBean 依赖 CertDigestBean 依赖 SystemService
    @Bean(name = "certDigestListBean")
    public CertDigestListBean certDigestListBean(List<CertDigestBean> certDigestBeanList){
        return new CertDigestListBean(certDigestBeanList);
    }
}
