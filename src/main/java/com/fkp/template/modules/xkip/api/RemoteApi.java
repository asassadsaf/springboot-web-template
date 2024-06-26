package com.fkp.template.modules.xkip.api;

import com.fkp.template.modules.xkip.dto.request.RemoteMethod2Request;
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
@FeignClient(name = "remoteApi", url = "${remote.restUrl}/xxx")
public interface RemoteApi {

    //GET请求需要添加@RequestParam，否则openfeign将自动转为POST请求
    @GetMapping(value = "/remoteMethod")
    String remoteMethod(@RequestParam(name = "name") String name);

    @PostMapping(value = "/remoteMethod2")
    String remoteMethod2(@RequestBody RemoteMethod2Request request);


}
