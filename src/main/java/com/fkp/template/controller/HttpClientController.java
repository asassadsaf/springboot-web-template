package com.fkp.template.controller;

import com.fkp.template.constant.CommonConstant;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    //GET
    public Map<String, String> get(){
        return null;
    }
    //POST-urlencoded

    //POST-form-data

    //POST-json


}
