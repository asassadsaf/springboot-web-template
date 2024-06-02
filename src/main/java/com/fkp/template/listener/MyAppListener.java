package com.fkp.template.listener;

import com.fkp.template.SpringBootWebTemplateApplication;
import com.fkp.template.constant.ErrorCodeEnum;
import com.fkp.template.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.InputStreamResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Security;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description Spring监听器-用于在SpringBoot生命周期某些阶段做初始化操作
 * @date 2024/5/21 20:06
 */
@Slf4j
public class MyAppListener implements SpringApplicationRunListener {
    private final SpringApplication application;

    private final String[] args;

    public MyAppListener(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
    }

    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        setApplicationPath();
        loadProvider();
    }

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        loadExternalConfig(environment);
    }

    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        registerPolicyFileListener(context);
    }

    private void loadExternalConfig(ConfigurableEnvironment environment){
        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        String applicationPath = environment.getProperty("application.path");
        String externalConfigPath = applicationPath + File.separator + "config" + File.separator + "application.yml";
        try (FileInputStream inputStream = new FileInputStream(externalConfigPath)){
            List<PropertySource<?>> externalConfigPropertySources = loader.load("externalConfig", new InputStreamResource(inputStream));
            environment.getPropertySources().addFirst(externalConfigPropertySources.get(0));
        } catch (IOException e) {
            log.error("Service start error, load external config file error. applicationPath: {}, externalConfigPath: {}", applicationPath, externalConfigPath);
            throw new BusinessException(ErrorCodeEnum.InternalServerError, "Service start error, load external config file error.", e);
        }
        log.info("load external config file success.");
    }

    private void registerPolicyFileListener(ConfigurableApplicationContext context){
        PolicyFileAlterationListener listener = context.getBean(PolicyFileAlterationListener.class);
        String filePath = StringUtils.substringBeforeLast(context.getEnvironment().getProperty("business.policy.path"), "/");
        if(StringUtils.isBlank(filePath)){
            log.error("Register policy file listener error policy file path is blank.");
            return;
        }
        // 使用过滤器
        File file = new File(filePath);
        FileAlterationObserver observer = new FileAlterationObserver(file);
        observer.addListener(listener);
        //创建文件变化监听器（默认为1000毫秒执行一次扫描）
        FileAlterationMonitor monitor = new FileAlterationMonitor(TimeUnit.SECONDS.toMillis(1), observer);
        // 开始监控
        try {
            monitor.start();
        } catch (Exception e) {
            log.error("Register policy file listener start monitor error. policyFilePath: {}", filePath, e);
        }
        listener.onFileChange(file);
    }

    //获取Jar包同级目录
    private void setApplicationPath(){
        ApplicationHome applicationHome = new ApplicationHome(SpringBootWebTemplateApplication.class);
        String applicationPath = applicationHome.getDir().getAbsolutePath();
        System.setProperty("application.path", applicationPath);
    }

    private void loadProvider(){
        if(Security.getProperty(BouncyCastleProvider.PROVIDER_NAME) == null){
            Security.addProvider(new BouncyCastleProvider());
        }
    }
}
