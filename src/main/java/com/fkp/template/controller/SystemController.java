package com.fkp.template.controller;

import com.alibaba.fastjson2.JSONObject;
import com.fkp.template.constant.CommonConstant;
import com.fkp.template.dto.response.SimpleRestResponse;
import com.fkp.template.entity.CertDigest;
import com.fkp.template.service.SystemService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.Yaml;

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
@Slf4j
public class SystemController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("classpath:config/policy/policy.yml")
    private Resource resource;

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

    //spring提供的读取classpath下文件方式
    @SneakyThrows
    @GetMapping(value = "/readClassPathFile")
    public String readClassPathFile(){
        //通过ResourceLoader对象读取
        Resource policyYmlResource = resourceLoader.getResource("classpath:config/policy/policy.yml");
        Yaml yaml = new Yaml();
        Object obj = yaml.load(policyYmlResource.getInputStream());
        log.info("classLoader get resource: {}", obj);

        //通过Resource类型和Value注解
        Yaml yaml2 = new Yaml();
        Object obj2 = yaml2.load(resource.getInputStream());
        log.info("Value annotation resource class type get resource: {}", obj2);
        JSONObject jsonObject  = new JSONObject();
        jsonObject.put("obj", obj);
        jsonObject.put("obj2", obj2);
        return jsonObject.toJSONString();
    }
}
