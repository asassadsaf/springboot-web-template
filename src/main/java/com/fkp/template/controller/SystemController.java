package com.fkp.template.controller;

import com.fkp.template.constant.CommonConstant;
import com.fkp.template.dto.response.SimpleRestResponse;
import com.fkp.template.entity.CertDigest;
import com.fkp.template.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/27 14:26
 */
@RestController
@RequestMapping(value = CommonConstant.BASE_URL + "/" + CommonConstant.VERSION_V1 + "/system", produces = MediaType.APPLICATION_JSON_VALUE)
public class SystemController {

    @Autowired
    private SystemService systemService;

    //获取证书和公钥指纹
    @GetMapping(value = "/generateCertDigest")
    public SimpleRestResponse<List<CertDigest>> generateCertDigest(){
        return systemService.generateCertDigest();
    }

    //策略配置
    @GetMapping(value = "queryPolicy")
    public SimpleRestResponse<Map<String, Object>> queryPolicy(){
        return systemService.queryPolicy();
    }

    @PostMapping(value = "configPolicy")
    public SimpleRestResponse<?> configPolicy(@RequestBody Map<String, Object> request){
        return systemService.configPolicy(request);
    }

}
