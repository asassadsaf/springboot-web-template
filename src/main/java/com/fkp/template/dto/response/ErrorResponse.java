package com.fkp.template.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/13 17:12
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class ErrorResponse extends BaseResponse implements Serializable {
    private static final long serialVersionUID = 2621638727631497841L;

    private final String Message;
    private final String Code;

    public ErrorResponse(String code, String message) {
        super();
        this.Message = message;
        this.Code = code;
    }
}
