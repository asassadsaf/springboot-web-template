package com.fkp.template.config.database;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description MybatisPlus配置类
 * @date 2024/6/20 20:43
 */
@Configuration(proxyBeanMethods = false)
public class MybatisPlusConfig {

    @Value("${database.type:sqlite}")
    private String databaseType;

    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.getDbType(databaseType)));
        return interceptor;
    }

    @Bean
    public MyIdentifierGenerator myIdentifierGenerator(){
        return new MyIdentifierGenerator();
    }

    @Bean
    public MybatisFieldFillHandler mybatisFieldFillHandler(){
        return new MybatisFieldFillHandler();
    }

    @Bean
    public MyVendorDatabaseIdProvider myVendorDatabaseIdProvider(){
        MyVendorDatabaseIdProvider provider = new MyVendorDatabaseIdProvider();
        provider.setDatabaseType(databaseType);
        return provider;
    }
}
