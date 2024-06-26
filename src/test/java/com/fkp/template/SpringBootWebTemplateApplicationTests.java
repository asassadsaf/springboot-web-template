package com.fkp.template;

import com.fkp.template.core.util.SynthesisCryptoUtils;
import com.fkp.template.modules.xkip.dto.response.SimpleRestResponse;
import com.fkp.template.modules.xkip.service.SystemService;
import com.fkp.template.core.util.SpringBeanUtils;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.net.URL;
import java.util.Map;

@SpringBootTest
class SpringBootWebTemplateApplicationTests {

    @Value("${business.shellPath.getSourceIps}")
    private String path;

    @SneakyThrows
    @Test
    void synthesisTest(){
        String s = SynthesisCryptoUtils.encPwd("SWXA1234@DAR_Mysql");
        System.out.println(s);
        String s1 = SynthesisCryptoUtils.decPwd(s);
        System.out.println(s1);
    }

    @Test
    void testGetSourceIps(){
        System.out.println(path);
        URL resource = SpringBootWebTemplateApplicationTests.class.getClassLoader().getResource(StringUtils.substringAfter(path, "classpath: "));
//        ExecUtils.execAndGetOutput()
        System.out.println(resource);
    }

    @Test
    void testDir(){
        System.out.println(System.getProperty("user.dir"));
        ApplicationHome home  = new ApplicationHome(SpringBootWebTemplateApplication.class);
        File dir = home.getDir();
        System.out.println(dir.getAbsolutePath());
    }

    @Test
    void testGetBean(){
        SystemService bean = SpringBeanUtils.getBean(SystemService.class);
        SimpleRestResponse<Map<String, Object>> mapSimpleRestResponse = bean.queryPolicy();
        System.out.println(mapSimpleRestResponse);
    }

    @Test
    void contextLoads() throws Exception {
//        Client client = new Client();
//        //String pathname, String method, java.util.Map<String, String> query, java.util.Map<String, String> headers,
//        // String signatureAlgorithm, String payload, String ak, byte[] signingkey, String product, String region, String date
//        String pathname = "/kms/xki/v1/keys/ExternalKeyId/meta";
//        String method = "POST";
//        String signatureAlgorithm = "ACS4-HMAC-SHA256";
//        String ak = "AccessKeyId";
//
//        //query parameter
//        Map<String, String> query = new LinkedHashMap<>();
//        query.put("Key2", "Value2");
//        query.put("Key1", "Value1");
//
//        //headers
//        Map<String, String> headers = new HashMap<>();
//        headers.put("x-acs-accesskey-id", "AccessKeyId");
//        headers.put("x-acs-action", "POST");
//        headers.put("host", "xki.example.com");
//        headers.put("x-acs-date", "20230928T080458Z");
//        headers.put("content-type", "application/json; charset=utf-8");
//        headers.put("x-acs-version", "1.0.0");
//        headers.put("x-acs-content-sha256", "50a36650aa0bfa2d833b7dfa32ce1c7ed0465a67cd870618a0147ae91f6c4898");
//        headers.put("x-acs-signature-nonce", "20230928-MkuBibyazkY1o2GJe");
//
//        //body
//        Map<String, Object> body = new LinkedHashMap<>();
////        Map<String, String> requestMetadata = new LinkedHashMap<>();
////        requestMetadata.put("PrincipalArn", "acs:kms:cn-hangzhou:123456789012:user/123456");
////        requestMetadata.put("SourceVpc", "vpc-12345678");
////        requestMetadata.put("SourceIp", "127.0.0.1");
////        requestMetadata.put("KmsInstanceId", "kst-1234567890");
////        requestMetadata.put("KmsKeyArn", "acs:kms:cn-hangzhou:123456789012:key/1111-1111-11111");
////        requestMetadata.put("KmsOperation", "kms:Encrypt");
////        requestMetadata.put("KmsRequestId", "req-9b32debc-6742-472b-85bd-bb4d8e01ed1e");
////        requestMetadata.put("KmsViaService", "kms");
////        body.put("RequestMetadata", requestMetadata);
////        body.put("Plaintext", "Aaa");
////        body.put("Algorithm", "AES_GCM");
////        body.put("externalKeyId", "123456789012");
//        body.put("TestParameter", "TestValue");
//
//        //payload
//        String jsonObj = com.aliyun.teautil.Common.toJSONString(body);
////        System.out.println(jsonObj);
//        System.out.println("payload: " + com.aliyun.darabonba.encode.Encoder.base64EncodeToString(com.aliyun.teautil.Common.toBytes(jsonObj)));
//        String hashedRequestPayload = com.aliyun.darabonba.encode.Encoder.hexEncode(com.aliyun.darabonba.encode.Encoder.hash(com.aliyun.teautil.Common.toBytes(jsonObj), signatureAlgorithm));
//        System.out.println("hashedRequestPayload: "+ hashedRequestPayload);
//
//        //signing key
//        String accessKeySecret = "AccessKeySecret";
//        String product = "kms";
//        String region = "cn-hangzhou";
//        String date = "20230928";
//        byte[] signingkey = client.getSigningkey(signatureAlgorithm, accessKeySecret, product, region, date);
//        System.out.println("signingkey: " + com.aliyun.darabonba.encode.Encoder.hexEncode(signingkey));
//
//        //authorization
//        String authorization = client.getAuthorization(pathname, method, query, headers, signatureAlgorithm, hashedRequestPayload, ak, signingkey, product, region, date);
//        System.out.println(authorization);


        String payload2 = "eyJSZXF1ZXN0TWV0YWRhdGEiOnsiUHJpbmNpcGFsQXJuIjoiYWNzOmttczpjbi1oYW5nemhvdToxMjM0NTY3ODkwMTI6dXNlci8xMjM0NTYiLCJTb3VyY2VWcGMiOiJ2cGMtMTIzNDU2NzgiLCJTb3VyY2VJcCI6IjEyNy4wLjAuMSIsIkttc0luc3RhbmNlSWQiOiJrc3QtMTIzNDU2Nzg5MCIsIkttc0tleUFybiI6ImFjczprbXM6Y24taGFuZ3pob3U6MTIzNDU2Nzg5MDEyOmtleS8xMTExLTExMTEtMTExMTEiLCJLbXNPcGVyYXRpb24iOiJrbXM6RW5jcnlwdCIsIkttc1JlcXVlc3RJZCI6InJlcS0xNjg4ZWI0My1lYzU1LTQ0YzgtOWYxZC1hMWQ5YTdjZjYwMjUiLCJLbXNWaWFTZXJ2aWNlIjoia21zIn0sIkxlbmd0aCI6NTEyLCJPdXRwdXRGb3JtIjoiUFVCTElDX0tFWV9XUkFQUEVEIiwiUHVibGljS2V5QmxvYiI6Ik1Ga3dFd1lIS29aSXpqMENBUVlJS29FY3oxVUJnaTBEUWdBRWJwc2ZTbXlxVjB4NjVKanhXMzZRT3FXTWlSQjB0bHZnSDBFWmJiOFdoZlFMSHEveEFMdDRrblorN3Q3TFMzWTdEVHEvYVRkZ1kraWpnWi9QbWZQckpnXHUwMDNkXHUwMDNkIiwiV3JhcHBpbmdBbGdvcml0aG0iOiJTTTJQS0UiLCJXcmFwcGluZ0tleVNwZWMiOiJFQ19TTTIifQ==";
        System.out.println(new String(Base64.decodeBase64(payload2)));
//        String payload3 = "eyJSZXF1ZXN0TWV0YWRhdGEiOnsiUHJpbmNpcGFsQXJuIjoiYWNzOmttczpjbi1oYW5nemhvdToxMjM0NTY3ODkwMTI6dXNlci8xMjM0NTYiLCJTb3VyY2VWcGMiOiJ2cGMtMTIzNDU2NzgiLCJTb3VyY2VJcCI6IjEyNy4wLjAuMSIsIkttc0luc3RhbmNlSWQiOiJrc3QtMTIzNDU2Nzg5MCIsIkttc0tleUFybiI6ImFjczprbXM6Y24taGFuZ3pob3U6MTIzNDU2Nzg5MDEyOmtleS8xMTExLTExMTEtMTExMTEiLCJLbXNPcGVyYXRpb24iOiJrbXM6RW5jcnlwdCIsIkttc1JlcXVlc3RJZCI6InJlcS1jOWYzMzkwNi00NWJmLTRjZWItOWI5MC00ZjMwYThjNjE2YTYiLCJLbXNWaWFTZXJ2aWNlIjoia21zIn0sIlBsYWludGV4dCI6IkFhYT0iLCJBbGdvcml0aG0iOiJBRVNfR0NNIiwiZXh0ZXJuYWxLZXlJZCI6IjEyMzQ1Njc4OTAxMiJ9";
//        System.out.println(new String(Encoder.base64Decode(payload3)));
    }

}
