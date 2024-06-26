package com.fkp.template.modules.xkip.dto.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fkp.template.core.constant.OutputFormEnum;
import lombok.*;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/14 15:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GenRandomRequest extends BaseRequest{
    //添加该注解，确保@Builder生成的建造器能够正常对改属性赋初值
    @Builder.Default
    private Integer Length = 32;
    @Builder.Default
    private String OutputForm = OutputFormEnum.PLAIN.name();
    private String PublicKeyBlob;
    private String WrappingAlgorithm;
    private String WrappingKeySpec;

}
