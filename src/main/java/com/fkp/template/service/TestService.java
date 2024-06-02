package com.fkp.template.service;

import com.fkp.template.dto.request.*;
import com.fkp.template.dto.response.*;

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
