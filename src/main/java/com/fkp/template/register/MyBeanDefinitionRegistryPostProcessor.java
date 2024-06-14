package com.fkp.template.register;

import com.fkp.template.entity.CertDigestBean;
import com.fkp.template.service.SystemService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 用于增删改Bean定义
 * @date 2024/6/12 16:05
 */
//@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        //只添加Bean定义，Bean的实例化交给Spring管理，避免出现Bean相互依赖导致无法注入的问题
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(CertDigestBean.class)
                //通过构造方法注入SystemService
//                .addConstructorArgReference("systemServiceImpl").addConstructorArgValue(0)
                //通过@Autowired注入无需额外操作
                //通过属性名注入
//                .addPropertyValue("alias", "abc")
                .getBeanDefinition();
        registry.registerBeanDefinition("fkpCertDigestBean", beanDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //在此处getBean会导致其中@Value的属性无法注入
//        System.out.println(beanFactory.getBean(SystemService.class));
    }
}
