package com.fkp.template.core.config.web;

import com.alibaba.fastjson2.JSONObject;
import com.fkp.template.core.constant.CommonConstant;
import com.fkp.template.core.constant.ErrorCodeEnum;
import com.fkp.template.core.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/13 15:43
 */
@Slf4j
@Component
public class MyInterceptor implements HandlerInterceptor {

    @Value("${business.specification}")
    private String specification;

    @Value("${business.serverInfo}")
    private String serverInfo;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request instanceof StandardMultipartHttpServletRequest){
            request = ((StandardMultipartHttpServletRequest) request).getRequest();
        }
        if(!(request instanceof MyHttpServletRequestWrapper)){
            throw new BusinessException(ErrorCodeEnum.Unauthorized, "request type invalid.");
        }
        MyHttpServletRequestWrapper requestWrapper = (MyHttpServletRequestWrapper) request;
        //鉴权逻辑
//        setHeaders2Response(requestWrapper, response);
//        setParam2RequestAttribute(requestWrapper);
//        String authorization = request.getHeader("authorization");
//        if(StringUtils.isBlank(authorization)){
//            throw new BusinessException(ErrorCodeEnum.Unauthorized, "authorization is blank.");
//        }
//        checkDate(requestWrapper);
//        doAuthroization(authorization, requestWrapper);
        return true;
    }

    private void checkDate(MyHttpServletRequestWrapper requestWrapper) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        SimpleTimeZone timeZone = new SimpleTimeZone(0, "UTC");
        sdf.setTimeZone(timeZone);
        Calendar calendar = Calendar.getInstance(timeZone);

        String clientDate = requestWrapper.getHeader("x-acs-date");
        long clientTime;
        try {
            clientTime = sdf.parse(clientDate).getTime();
        } catch (ParseException e) {
            throw new BusinessException(ErrorCodeEnum.InvalidAcsDate, "parse acs date error.", e);
        }
        long serverTime = calendar.getTimeInMillis();
        if(Math.abs(serverTime - clientTime) > 15 * 60 * 1000){
            throw new BusinessException(ErrorCodeEnum.InvalidAcsDate, "The difference between client time and server time exceeds 15 minutes. currentDate: %s", sdf.format(calendar.getTime()));
        }
    }

    private void doAuthroization(String authorization, MyHttpServletRequestWrapper request) {
        String newAuthorization;
        try {
            newAuthorization = generateAuthorization(authorization, request);
        } catch (BusinessException e) {
            throw e;
        }catch(Exception e) {
            throw new BusinessException(ErrorCodeEnum.Unauthorized, "generate authorization error.", e);
        }
        if(!authorization.equals(newAuthorization)){
            log.error("do authorization error, authorization: {}, newAuthorization: {}", authorization, newAuthorization);
            throw new BusinessException(ErrorCodeEnum.Unauthorized, "authorization not match.");
        }
    }

    private String generateAuthorization(String authorization, MyHttpServletRequestWrapper request) throws Exception {
        return null;
    }


    private Map<String, String> generateParameterMap(MyHttpServletRequestWrapper requestWrapper) {
        Map<String, String> query = new LinkedHashMap<>();
        Enumeration<String> parameterNames = requestWrapper.getParameterNames();
        while (parameterNames.hasMoreElements()){
            String key = parameterNames.nextElement();
            query.put(key, requestWrapper.getParameterValues(key)[0]);
        }
        log.debug("generate parameter map result: {}", query);
        return query;
    }

    private Map<String, String> generateHeadersMap(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>(8);
        headers.put("x-acs-accesskey-id", request.getHeader("x-acs-accesskey-id"));
        headers.put("x-acs-action", request.getHeader("x-acs-action"));
        headers.put("host", request.getHeader("host"));
        headers.put("x-acs-date", request.getHeader("x-acs-date"));
        headers.put("content-type", request.getHeader("content-type"));
        headers.put("x-acs-version", request.getHeader("x-acs-version"));
        String sha256Content = request.getHeader("x-acs-content-sha256");
        String sm3Content = request.getHeader("x-acs-content-sm3");
        if(StringUtils.isNotBlank(sha256Content)){
            headers.put("x-acs-content-sha256", sha256Content);
        }else if(StringUtils.isNotBlank(sm3Content)){
            headers.put("x-acs-content-sm3", sm3Content);
        }else {
            throw new BusinessException(ErrorCodeEnum.Unauthorized, "Request header miss x-acs-content-sm3 or x-acs-content-sha256");
        }
        headers.put("x-acs-signature-nonce", request.getHeader("x-acs-signature-nonce"));
        log.debug("generate headers map result: {}", headers);
        return headers;
    }

    private void setHeaders2Response(MyHttpServletRequestWrapper request, HttpServletResponse response){
        Object requestMetadata = request.getBodyParam("RequestMetadata");
        if(!(requestMetadata instanceof JSONObject)){
            throw new BusinessException(ErrorCodeEnum.RequestMetadataRequired, "RequestMetadata is null or not object.");
        }
        String kmsRequestId = ((JSONObject)requestMetadata).getString("KmsRequestId");
        if(StringUtils.isBlank(kmsRequestId)){
            throw new BusinessException(ErrorCodeEnum.InvalidRequestMetadata, "KmsRequestId is blank.");
        }
        response.setHeader("x-xki-kms-request-id", kmsRequestId);
        response.setHeader("x-xki-specification", specification);
        response.setHeader("x-xki-server", serverInfo);
    }

    private void setParam2RequestAttribute(MyHttpServletRequestWrapper request){
        request.setAttribute(CommonConstant.REQUEST_METADATA_NAME, request.getBodyParam("RequestMetadata"));
        request.setAttribute(CommonConstant.KEY_ID_NAME, request.getBodyParam("externalKeyId"));
    }

    private String getSignatureAlgorithm(String authorization) {
        String alg = authorization.split(" ")[0];
        if(CommonConstant.HMAC_SHA256.equals(alg) || CommonConstant.HMAC_SM3.equals(alg)){
            return alg;
        }
        log.error("getSignatureAlgorithm from request header authorization error. not match {} or {}, realAlg: {}, authorization: {}",
                CommonConstant.HMAC_SHA256, CommonConstant.HMAC_SM3, alg, authorization);
        throw new BusinessException(ErrorCodeEnum.InvalidParameter, "SignatureAlgorithm is not match.");
    }
}
