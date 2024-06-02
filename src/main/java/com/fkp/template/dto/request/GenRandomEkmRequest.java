package com.fkp.template.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/21 19:13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenRandomEkmRequest {
    private Integer length;
    private String spec;
    private String alg;
    private String blob;
}
