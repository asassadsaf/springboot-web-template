package com.fkp.template.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.ParameterMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.MimeHeaders;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;
import java.io.*;
import java.util.*;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description request对象包装类，用于获取请求头请求参数
 * @date 2024/4/29 10:12
 */
@Slf4j
public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final ParameterMap<String, String[]> parameterMap;

    private final MimeHeaders headers;

    private final byte[] data;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public MyHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        parameterMap = new ParameterMap<>();
        headers = new MimeHeaders();
        data = getRequestBuf2Data(request);
    }

    private byte[] getRequestBuf2Data(HttpServletRequest request) {
        try {
            //先调用getParameterNames触发org.apache.catalina.connector#parseParameters方法，将parametersParsed设为true
            //否则调用getInputStream会导致usingInputStream设为true，然后在调用parseParameters方法时检测到usingInputStream为true直接返回
            //POST(form-data)类型同理
            request.getParameterNames();
            ServletInputStream inputStream = request.getInputStream();
            byte[] buf = new byte[1024];
            ByteArrayOutputStream bis = new ByteArrayOutputStream();
            int len;
            while ((len = inputStream.read(buf)) != -1){
                bis.write(buf, 0 ,len);
            }
            return bis.toByteArray();
        }catch (IOException e){
            log.error("Get data from request input stream error.", e);
        }
        return new byte[0];
    }

    /**
     * 获取Get请求参数，若存在同名key，优先级 包装 > 原生
     * @param name 参数名
     * @return 参数值
     */
    @Override
    public String getParameter(String name) {
        String[] values = parameterMap.get(name);
        String realValue = values == null ? null : values.length > 0 ? values[0] : "";
        return Optional.ofNullable(realValue).orElse(super.getParameter(name));
    }

    /**
     * 无调用， 若存在同名key，优先级 包装 > 原生
     * @return 参数值
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        //若存在相同key，super.getParameterMap()覆盖parameterMap
        Map<String, String[]> res = new LinkedHashMap<>();
        res.putAll(super.getParameterMap());
        res.putAll(parameterMap);
        return res;
    }

    /**
     * 获取Post(x-www-form-urlencoded,form-data(除file类型))所有参数名
     * @return 所有参数名的Enumeration类型
     */
    @Override
    public Enumeration<String> getParameterNames() {
        HashSet<String> set = new HashSet<>();
        Enumeration<String> parameterNames = super.getParameterNames();
        while (parameterNames.hasMoreElements()){
            set.add(parameterNames.nextElement());
        }
        set.addAll(parameterMap.keySet());
        return Collections.enumeration(set);
    }

    /**
     * 获取Get请求参数的值,若存在同名key，优先级 包装 > 原生
     * Get/Post(x-www-form-urlencoded,form-data(除file类型))请求在映射到Controller参数之前会调用该方法取参数
     * @param name 参数名
     * @return 参数值数组
     */
    @Override
    public String[] getParameterValues(String name) {
        return Optional.ofNullable(parameterMap.get(name)).orElse(super.getParameterValues(name));
    }

    /**
     * 设置Get/Post(x-www-form-urlencoded)请求参数
     * @param name 参数名
     * @param value 参数值
     */
    public void setParameter(String name, String value){
        if(StringUtils.isBlank(name)){
            throw new IllegalArgumentException("set parameter: name can not blank.");
        }
        parameterMap.put(name, new String[]{value});
    }

    /**
     * 获取请求头的值，若存在相同名称，优先级 包装 > 原生
     * @param name 参数名
     * @return 参数值
     */
    @Override
    public String getHeader(String name) {
        return Optional.ofNullable(headers.getHeader(name)).orElse(super.getHeader(name));
    }

    /**
     * 获取请求头的值，可能为多个，若存在相同名称，优先级 包装 > 原生
     * @param name 参数名
     * @return 参数值
     */
    @Override
    public Enumeration<String> getHeaders(String name) {
        return Optional.ofNullable(headers.values(name)).orElse(super.getHeaders(name));
    }

    /**
     * 获取所有请求头名称，去重
     * @return 所有去重后的请求头名称
     */
    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> set = new HashSet<>();
        Enumeration<String> headerNamesEnum = headers.names();
        Enumeration<String> headerNamesEnum2 = super.getHeaderNames();
        while (headerNamesEnum.hasMoreElements()){
            set.add(headerNamesEnum.nextElement());
        }
        while (headerNamesEnum2.hasMoreElements()){
            set.add(headerNamesEnum2.nextElement());
        }
        return Collections.enumeration(set);
    }

    /**
     * 设置请求头，若名称重复则覆盖
     * @param name 参数名
     * @param value 参数值，若已经存在相同的name则覆盖原来的value
     */
    public void setHeader(String name, String value){
        if(StringUtils.isBlank(name)){
            throw new IllegalArgumentException("set parameter: name can not blank.");
        }
        MessageBytes messageBytes = headers.setValue(name);
        messageBytes.setString(value);

    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        return new ServletInputStream(){

            @Override
            public int read() throws IOException {
                return bis.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public void close() throws IOException {
                super.close();
                bis.close();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        ServletInputStream inputStream = getInputStream();
        return new BufferedReader(new InputStreamReader(inputStream){
            @Override
            public void close() throws IOException {
                super.close();
                inputStream.close();
            }
        });
    }

    public Map<String, Object> getBodyParamMap(){
        String contentType = getContentType();
        if(StringUtils.isBlank(contentType)){
            return null;
        }
        String contentTypeLower = contentType.toLowerCase(Locale.ROOT);
        //raw: text(json) json
        //只考虑json为object的情况，json array不考虑
        if(contentTypeLower.contains(MediaType.TEXT_PLAIN_VALUE) || contentTypeLower.contains(MediaType.APPLICATION_JSON_VALUE)){
            String str;
            try {
                str = new String(data, getCharacterEncoding());
            } catch (UnsupportedEncodingException e) {
                log.error("convert bytes data to string error.", e);
                return null;
            }
            //Object
            if(!str.startsWith("{")){
                log.error("parse json str to object error, str not start with '{'. str: {}", str);
                return null;
            }
            JSONObject jsonObject;
            try {
                jsonObject = JSON.parseObject(str);
            }catch (Exception e){
                log.error("parse json str to object error. str: {}", str, e);
                return null;
            }
            return Collections.unmodifiableMap(jsonObject);
        }
        return null;
    }

    public Object getBodyParam(String name){
        return getBodyParamMap().get(name);
    }

    /**
     * POST(form-data)请求类型时调用改方法用来构造StandardMultipartHttpServletRequest对象
     * @return 包含请求参数(普通参数parameter以及文件参数file)的Part对象的集合
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return super.getParts();
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        return super.getPart(name);
    }
}
