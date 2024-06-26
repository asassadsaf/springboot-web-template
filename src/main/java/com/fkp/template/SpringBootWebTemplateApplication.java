package com.fkp.template;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description springboot入口
 * @date 2024/5/13 16:50
 */

@SpringBootApplication
//扫描servlet过滤器
@ServletComponentScan(basePackages = "com.fkp.template.core.config.web")
//扫描feign接口
@EnableFeignClients(basePackages = "com.fkp.template.modules.*.api")
//扫描mapper接口
@MapperScan(basePackages = "com.fkp.template.modules.*.mapper")
@EnableScheduling
public class SpringBootWebTemplateApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringBootWebTemplateApplication.class, args);

    }

}
