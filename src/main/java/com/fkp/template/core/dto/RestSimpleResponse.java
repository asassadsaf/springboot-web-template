package com.fkp.template.core.dto;

import com.fkp.template.core.constant.RestErrorEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description Rest接口简单响应类
 * @date 2024/6/26 13:51
 */

@Data
public class RestSimpleResponse<T> {
    private static final long serialVersionUID = 2621638727631497840L;
    private String code;
    private String message;
    private T data;
    private static final String CODE_SUCCESS = "0";
    private static final String MESSAGE_SUCCESS = "operation success";

    private RestSimpleResponse() {}

    public static <T> RestSimpleResponse<T> success(){
        RestSimpleResponse<T> res = new RestSimpleResponse<>();
        res.setCode(CODE_SUCCESS);
        res.setMessage(MESSAGE_SUCCESS);
        return res;
    }

    public static <T> RestSimpleResponse<T> success(T data){
        RestSimpleResponse<T> res = new RestSimpleResponse<>();
        res.setCode(CODE_SUCCESS);
        res.setMessage(MESSAGE_SUCCESS);
        res.setData(data);
        return res;
    }

    public static <T> RestSimpleResponse<T> fail(RestErrorEnum errorEnum){
        RestSimpleResponse<T> res = new RestSimpleResponse<>();
        res.setCode(errorEnum.getCode());
        res.setMessage(errorEnum.getMsg());
        return res;
    }

}
