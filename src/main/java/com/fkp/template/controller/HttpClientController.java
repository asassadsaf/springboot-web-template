package com.fkp.template.controller;

import com.alibaba.fastjson2.JSONObject;
import com.fkp.template.constant.CommonConstant;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
public class HttpClientController {

    //GET-header
    @GetMapping(value = "/get")
    public Map<String, Object> get(@RequestParam("name") String name, HttpServletRequest request){
        String token = request.getHeader("token");
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
        return JSONObject.of("name", name, "age", age, "file", IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8), "token", request.getHeader("token"));
    }

    //POST-json
    @PostMapping(value = "/postJson")
    public Map<String, Object> postJson(@RequestBody Map<String, Object> param, HttpServletRequest request){
        param.put("token", request.getHeader("token"));
        return param;
    }

}
