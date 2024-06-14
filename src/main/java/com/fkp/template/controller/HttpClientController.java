package com.fkp.template.controller;

import com.alibaba.fastjson2.JSONObject;
import com.fkp.template.constant.CommonConstant;
import org.apache.commons.collections.MapUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
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

    //POST-form-data

    //POST-json


}
