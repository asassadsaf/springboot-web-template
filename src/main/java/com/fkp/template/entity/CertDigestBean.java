package com.fkp.template.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 用来测试自定义注册bean
 * @date 2024/5/28 10:38
 */
@Data
@Builder
@NoArgsConstructor
@Slf4j
public class CertDigestBean {
    private String alias;
    private String certDigestSha256;
    private String certDigestSm3;
    private String publicKeyDigestSha256;
    private String publicKeyDigestSm3;

    public CertDigestBean(String alias, String certDigestSha256, String certDigestSm3, String publicKeyDigestSha256, String publicKeyDigestSm3) {
        this.alias = alias;
        this.certDigestSha256 = certDigestSha256;
        this.certDigestSm3 = certDigestSm3;
        this.publicKeyDigestSha256 = publicKeyDigestSha256;
        this.publicKeyDigestSm3 = publicKeyDigestSm3;
        log.info("CertDigestBean constructor method call. content: {}", this);
    }
}
