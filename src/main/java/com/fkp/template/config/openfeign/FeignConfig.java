package com.fkp.template.config.openfeign;

import cn.hutool.http.ssl.TrustAnyHostnameVerifier;
import com.fkp.template.constant.ErrorCodeEnum;
import com.fkp.template.exception.BusinessException;
import feign.Client;
import feign.Logger;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/15 21:05
 */
@Configuration(proxyBeanMethods = false)
public class FeignConfig {

    //配置openfeign日志，FULL为全部日志，输出受日志框架的日志级别限制
    @Bean
    public Logger.Level openFeignLogger(){
        return Logger.Level.FULL;
    }

    //openfeign调用添加签名值
    @Component
    public static class GenSignHeaderFeignInterceptor extends FeignSignOnlyRequestInterceptor {

        @Override
        public void apply(RequestTemplate template) {
            template.header("lang", "en_US");
            super.apply(template);
        }
    }

    //openfeign调用支持https
    @Bean
    public Client feignClient(){
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}
                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            TrustManager[] tms = new TrustManager[]{tm};
            context.init(null, tms, null);
            SSLSocketFactory socketFactory = context.getSocketFactory();
            return new Client.Default(socketFactory, new TrustAnyHostnameVerifier());
        }catch (Exception e){
            throw new BusinessException(ErrorCodeEnum.InternalServerError, "XKIP service start error, load bean open feign ssl client failed.", e);
        }
    }
}
