package com.fkp.template.config;

import com.fkp.template.util.SignUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description openfeign拦截器-请求头添加签名信息
 * @date 2024/5/29 17:29
 */

public class FeignSignOnlyRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String randKey = String.valueOf(ThreadLocalRandom.current().nextDouble());
        template.header(SignUtils.TIMESTAMP_KEY, timestamp);
        template.header(SignUtils.RAND_KEY, randKey);
        TreeMap<String, String> h = new TreeMap<>();
        h.put(SignUtils.TIMESTAMP_KEY, timestamp);
        h.put(SignUtils.RAND_KEY, randKey);
        template.header(SignUtils.SIGN_KEY, SignUtils.sign(h));
    }

}
