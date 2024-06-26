package com.fkp.template.modules.xkip.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/27 17:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class SimpleRestResponse<T> {
    private String Message;
    private T data;
    private static final String SUCCESS_MESSAGE = "success";

    public static <T> SimpleRestResponse<T> success(){
        return new SimpleRestResponse<>(SUCCESS_MESSAGE, null);
    }

    public static <T> SimpleRestResponse<T> success(T data){
        return new SimpleRestResponse<>(SUCCESS_MESSAGE, data);
    }
}
