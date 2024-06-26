package com.fkp.template.modules.xkip.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/14 15:47
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class GenRandomResponse extends BaseResponse{
    private String OutputForm;
    private String Output;
}
