package com.fkp.template.core.listener;

import com.fkp.template.core.constant.ErrorCodeEnum;
import com.fkp.template.core.util.SynthesisCryptoUtils;
import com.fkp.template.modules.xkip.entity.CertDigest;
import com.fkp.template.modules.xkip.entity.CertDigestBean;
import com.fkp.template.core.exception.BusinessException;
import com.fkp.template.modules.xkip.service.SystemService;
import com.sansec.jce.provider.SwxaProvider;
import com.sansec.tlcp.jsse.provider.SwxaJsseProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.opengauss.log.Log;
import org.opengauss.log.Logger;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
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
        // 初始化合成分量加解密工具类
        initSynthesis(environment);
        sslLogConfig(environment);
    }

    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        registerPolicyFileListener(context);
        //容器启动完成后注册Bean定义，在首次使用Bean时进行初始化
//        registerMyBean(context);
    }

    private void initSynthesis(ConfigurableEnvironment environment) {
        String lmk1 = environment.getProperty("synthesis.lmk1");
        String filePath = environment.getProperty("synthesis.lmk2FilePath");
        try {
            SynthesisCryptoUtils.init(lmk1, filePath);
        }catch (Exception e){
            log.error("init synthesis key error.", e);
        }
    }

    private void sslLogConfig(ConfigurableEnvironment environment){
        String javaxNetDebug = environment.getProperty("logging.javaxNetDebug", "false");
        System.setProperty("javax.net.debug", javaxNetDebug);
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
        ApplicationHome applicationHome = new ApplicationHome();
        String applicationPath = applicationHome.getDir().getAbsolutePath();
        System.setProperty("application.path", applicationPath);
    }

    private void loadProvider(){
        if(Security.getProperty(BouncyCastleProvider.PROVIDER_NAME) == null){
            Security.addProvider(new BouncyCastleProvider());
        }
//        if(Security.getProvider("SwxaJCE") == null){
//            Security.addProvider(new SwxaProvider());
//        }
//        if(Security.getProvider("SwxaJSSE") == null){
//            Security.addProvider(new SwxaJsseProvider());
//        }
    }

    private void registerMyBean(ConfigurableApplicationContext context){
        if(context instanceof GenericApplicationContext){
            GenericApplicationContext genericApplicationContext = (GenericApplicationContext) context;
            //在ready阶段可以拿到ioc中的bean
            SystemService systemService = context.getBean(SystemService.class);
            List<CertDigest> data = systemService.generateCertDigest().getData();
            CertDigest certDigest = data.get(0);
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(CertDigestBean.class);
            beanDefinitionBuilder.addConstructorArgValue(certDigest.getAlias());
            beanDefinitionBuilder.addConstructorArgValue(certDigest.getCertDigestSha256());
            beanDefinitionBuilder.addConstructorArgValue(certDigest.getCertDigestSm3());
            beanDefinitionBuilder.addConstructorArgValue(certDigest.getPublicKeyDigestSha256());
            beanDefinitionBuilder.addConstructorArgValue(certDigest.getPublicKeyDigestSm3());
            //注册自定义的bean定义，此时还没有实例化bean，在首次使用到该bean时进行实例化
            genericApplicationContext.registerBeanDefinition("fkpCertDigestBean", beanDefinitionBuilder.getBeanDefinition());
            //获取该bean，触发实例化
            context.getBean("fkpCertDigestBean");
        }
    }
}
