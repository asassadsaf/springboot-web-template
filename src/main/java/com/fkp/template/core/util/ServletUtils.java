package com.fkp.template.core.util;

import com.alibaba.fastjson2.JSON;
import com.fkp.template.core.dto.RestSimpleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 客户端工具类
 * @author fengkunpeng
 */
@Slf4j
public class ServletUtils {

    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     */
    public static void renderString(HttpServletResponse response, RestSimpleResponse<?> data) {
        try {
            response.setStatus(200);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().print(JSON.toJSONString(data));
        } catch (IOException e) {
            log.error("write data to response error.", e);
        }
    }

}
