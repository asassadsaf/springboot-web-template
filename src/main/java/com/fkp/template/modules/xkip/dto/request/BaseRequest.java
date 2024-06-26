package com.fkp.template.modules.xkip.dto.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/14 13:57
 */
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class BaseRequest {
    private RequestMetadata RequestMetadata;
}
