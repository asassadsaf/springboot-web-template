package com.fkp.template.modules.xkip.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class BaseResponse implements Serializable {
    private static final long serialVersionUID = 2621638727631497840L;
    private final String RequestId;

    protected BaseResponse() {
        this.RequestId = UUID.randomUUID().toString();
    }

}
