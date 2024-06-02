package com.fkp.template.controller;

import com.fkp.template.dto.request.*;
import com.fkp.template.dto.response.*;
import com.fkp.template.util.ValidateUtils;
import com.fkp.template.constant.CommonConstant;
import com.fkp.template.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/14 11:21
 */
@RestController
@RequestMapping(value = CommonConstant.BASE_URL + "/" + CommonConstant.VERSION_V1, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping(value = "/health")
    public XkiHealthResponse health(@RequestBody BaseRequest request){
        return testService.health(request.getRequestMetadata());
    }

    @PostMapping(value = "/random")
    public GenRandomResponse random(@RequestBody GenRandomRequest request){
        ValidateUtils.wrapRandomLength(request.getLength(), request.getWrappingAlgorithm());
        return testService.random(request);
    }

}
