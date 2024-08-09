package com.fkp.template.modules.httpclient.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fkp.template.core.config.web.MyHttpServletRequestWrapper;
import com.fkp.template.core.constant.CommonConstant;
import com.fkp.template.core.constant.RestErrorEnum;
import com.fkp.template.core.exception.RestBusinessException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 测试http client请求 get/post-json/post-formdata/post-urlencoded
 * @date 2024/6/6 16:41
 */
@RestController
@RequestMapping(value = CommonConstant.BASE_URL + "/" + CommonConstant.VERSION_V1 + "/client", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class HttpClientController {

    //GET-header
    @GetMapping(value = "/get")
    public Map<String, Object> get(@RequestParam("name") String name, HttpServletRequest request){
        String token = request.getHeader("token");
        if("fkp".equals(name)){
            throw new RestBusinessException(RestErrorEnum.INTERNAL_SERVER_ERROR);
        }
        return JSONObject.of("token", token, "name", name);
    }
    //POST-urlencoded
    @PostMapping(value = "/postUrlEncoded")
    public Map<String, Object> postUrlEncoded(String name, Integer age, HttpServletRequest request){
        return JSONObject.of("name", name, "age", age, "token", request.getHeader("token"));
    }

    //POST-form-data
    @SneakyThrows
    @PostMapping(value = "/postFormData")
    public Map<String, Object> postFormData(String name, Integer age, MultipartFile file, HttpServletRequest request){
        String content;
        try (InputStream inputStream = file.getInputStream()){
            // toString方法不关闭stream流，若不手动关闭会导致保存在tomcat中的临时文件无法删除
            content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
        return JSONObject.of("name", name, "age", age, "file", content, "token", request.getHeader("token"));
    }

    //POST-json
    @PostMapping(value = "/postJson")
    public Map<String, Object> postJson(@RequestBody Map<String, Object> param, HttpServletRequest request){
        param.put("token", request.getHeader("token"));
        return param;
    }

    //POST请求，通过url传递参数时，body体为空，参数存在parameter中
    @PostMapping(value = "/postParam")
    public Map<String, Object> postParam(@RequestParam(value = "param") String jsonParam, HttpServletRequest request){
        MyHttpServletRequestWrapper wrapper = (MyHttpServletRequestWrapper) request;
        //此时contentType为空，这里bodyParamMap为空Map,内部会打印错误日志
        Map<String, Object> bodyParamMap = wrapper.getBodyParamMap();
        Map<String, String[]> parameterMap = wrapper.getParameterMap();
        log.info("bodyParamMap: {}", bodyParamMap);
        log.info("paramMap: {}", parameterMap);
        //此时setBodyParam不会做任何操作吗，内部会打印错误日志
        wrapper.setBodyParam("addr", "jinan");
        log.info("bodyParam addr: {}", wrapper.getBodyParam("addr"));
        return JSON.parseObject(jsonParam);
    }

}
