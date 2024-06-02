package com.fkp.template.api;

import com.fkp.template.dto.request.GenRandomEkmRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description openfeign调用接口
 * @date 2024/5/17 15:57
 */
@FeignClient(name = "xkipApi", url = "${remote.restUrl}/xkip/ali")
public interface XkipApi {

    //GET请求需要添加@RequestParam，否则openfeign将自动转为POST请求
    @GetMapping(value = "/keyMeta")
    Object getKeyMeta(@RequestParam(name = "externalKeyId") String externalKeyId);

    @PostMapping(value = "/random")
    String generateRandom(@RequestBody GenRandomEkmRequest request);


}
