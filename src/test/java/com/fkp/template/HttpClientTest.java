package com.fkp.template;

import com.fkp.tools.openfeign.util.HttpClient5Utils;
import com.sansec.jce.provider.SwxaProvider;
import com.sansec.tlcp.jsse.provider.SwxaJsseProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Security;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/11/21 16:20
 */
@SpringBootTest
public class HttpClientTest {

    @Autowired
    private HttpClient5Utils httpClient5Utils;

    @BeforeAll
    static void init(){
        Security.addProvider(new SwxaProvider());
        Security.addProvider(new SwxaJsseProvider());
    }

    @Test
    void test(){
        Map<String, Object> body = new HashMap<>();
        body.put("keyName", "dsadas");
        Map<String, Object> headers = new HashMap<>();
        headers.put("X-SW-Authorization-Token", "eyJhbGciOiJTTTJXaXRoU00zIn0.eyJzdWIiOiJmMTA2MTM5MC0xMmVhLTdlMDUtYjU2OC1jNDkyNzcyYTNjODEiLCJhdWQiOiJmMTA2MTM5MC0xMmVhLTdlMDUtYjU2OC1jNDkyNzcyYTNjODEiLCJuYmYiOjE3MzIxNTU1MTEsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6OTAwMC9jY3NwL2F1dGgiLCJleHAiOjE3MzI3NjAzMTEsInR5cGUiOiJBS1NLIiwiaWF0IjoxNzMyMTU1NTExLCJzYW5zZWNfY3VzdG9tX3BhcmFtcyI6ImYxMDYxMzkwLTEyZWEtN2UwNS1iNTY4LWM0OTI3NzJhM2M4MSJ9.u3egvjBG0wjH93AC-ZrnZDSdSdPujegZ8EoDl6g_wCcFeXy_AiWhpjaHjFgnHmNMwa1Kdoi_TLAcQrdlmVTa9g");
        Map<String, Object> map = httpClient5Utils.postJson("https://10.0.120.104:20121/kms/v4/keys/get", body, headers);
        System.out.println(map);

    }
}
