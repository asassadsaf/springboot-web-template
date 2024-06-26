package com.fkp.template.modules.xkip.service;

import com.fkp.template.modules.xkip.dto.request.GenRandomRequest;
import com.fkp.template.modules.xkip.dto.request.RequestMetadata;
import com.fkp.template.modules.xkip.dto.response.GenRandomResponse;
import com.fkp.template.modules.xkip.dto.response.XkiHealthResponse;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/14 15:54
 */
public interface TestService {
    XkiHealthResponse health(RequestMetadata requestMetadata);

    GenRandomResponse random(GenRandomRequest request);

}
