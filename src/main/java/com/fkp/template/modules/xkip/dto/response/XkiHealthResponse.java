package com.fkp.template.modules.xkip.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/14 15:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class XkiHealthResponse extends BaseResponse{
    private String Specification;
    private String ServerName;
    private String ServerVersion;
    private String Vendor;
    private String Status;
    private String Message;
}
