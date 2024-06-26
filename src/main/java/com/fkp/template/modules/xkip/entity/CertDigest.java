package com.fkp.template.modules.xkip.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/28 10:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertDigest {
    private String alias;
    private String certDigestSha256;
    private String certDigestSm3;
    private String publicKeyDigestSha256;
    private String publicKeyDigestSm3;
}
