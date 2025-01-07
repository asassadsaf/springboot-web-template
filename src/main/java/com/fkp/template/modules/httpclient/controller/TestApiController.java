package com.fkp.template.modules.httpclient.controller;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/12/24 15:06
 */
@RestController
@RequestMapping(value = "/ccsp/pt/report/v1", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class TestApiController {

    @PostMapping(value = "/keylifecycle")
    public JSONObject keylifecycle(@RequestBody Map<String, Object> params){
        log.info("received data: {}", params);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "0");
        jsonObject.put("message", "操作成功");
        return jsonObject;
    }
}
