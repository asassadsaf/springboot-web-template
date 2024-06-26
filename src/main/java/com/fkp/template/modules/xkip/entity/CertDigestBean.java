package com.fkp.template.modules.xkip.entity;

import com.fkp.template.modules.xkip.service.SystemService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 用来测试自定义注册bean
 * @date 2024/5/28 10:38
 */
@Data
@NoArgsConstructor
@Slf4j
public class CertDigestBean {
    //属性可通过属性注入addPropertyValue(fieldName, value)
    private String alias;
    private String certDigestSha256;
    private String certDigestSm3;
    private String publicKeyDigestSha256;
    private String publicKeyDigestSm3;

    //通过@Autowired注入SystemService
    @Autowired
    private SystemService systemService;

    //通过构造方法注入SystemService
//    public CertDigestBean(SystemService systemService, int index) {
//        CertDigest certDigest = systemService.generateCertDigest().getData().get(index);
//        this.alias = certDigest.getAlias();
//        this.certDigestSha256 = certDigest.getCertDigestSha256();
//        this.certDigestSm3 = certDigest.getCertDigestSm3();
//        this.publicKeyDigestSha256 = certDigest.getPublicKeyDigestSha256();
//        this.publicKeyDigestSm3 = certDigest.getPublicKeyDigestSm3();
//        log.debug("CertDigestBean constructor method call. content: {}", this);
//    }

    public CertDigestBean instance(int index) {
        CertDigest certDigest = systemService.generateCertDigest().getData().get(index);
        this.alias = certDigest.getAlias();
        this.certDigestSha256 = certDigest.getCertDigestSha256();
        this.certDigestSm3 = certDigest.getCertDigestSm3();
        this.publicKeyDigestSha256 = certDigest.getPublicKeyDigestSha256();
        this.publicKeyDigestSm3 = certDigest.getPublicKeyDigestSm3();
        log.debug("CertDigestBean constructor method call. content: {}", this);
        return this;
    }
}
