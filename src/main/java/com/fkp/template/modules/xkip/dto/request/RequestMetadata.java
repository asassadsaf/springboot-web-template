package com.fkp.template.modules.xkip.dto.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/14 13:56
 */
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RequestMetadata {
    private String PrincipalArn;
    private String SourceVpc;
    private String KmsInstanceId;
    private String KmsKeyArn;
    private String KmsOperation;
    private String KmsRequestId;
    private String KmsViaService;
}
