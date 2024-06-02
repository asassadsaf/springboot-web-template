package com.fkp.template.service;

import com.fkp.template.dto.response.SimpleRestResponse;
import com.fkp.template.entity.CertDigest;

import java.util.List;
import java.util.Map;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/27 14:30
 */
public interface SystemService {

    SimpleRestResponse<List<String>> queryIpWhiteList();

    SimpleRestResponse<List<CertDigest>> generateCertDigest();


    SimpleRestResponse<Map<String, Object>> queryPolicy();


    SimpleRestResponse<?> configPolicy(Map<String, Object> request);
}
