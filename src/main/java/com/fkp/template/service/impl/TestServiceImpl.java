package com.fkp.template.service.impl;

import com.fkp.template.constant.*;
import com.fkp.template.dto.request.*;
import com.fkp.template.dto.response.*;
import com.fkp.template.util.KeyUtils;
import com.fkp.template.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/14 15:54
 */
@Service
@Slf4j
public class TestServiceImpl implements TestService {

    @Value("${business.specification}")
    private String specification;

    @Value("${business.serverName}")
    private String serverName;

    @Value("${business.serverVersion}")
    private String serverVersion;

    @Value("${business.vendor}")
    private String vendor;

    @Override
    public XkiHealthResponse health(RequestMetadata requestMetadata) {
        String xkiStatus = XkiStatusEnum.ACTIVE.name();
        String xkiMessage = CommonConstant.OK;
        try {
            //call service
        }catch (Exception e){
            xkiStatus = XkiStatusEnum.UNAVAILABLE.name();
            xkiMessage = "Call ekm get health status error.";
            log.error(xkiMessage, e);
        }
        return XkiHealthResponse.builder().XkiSpecification(specification).XkiServerName(serverName).XkiServerVersion(serverVersion)
                .XkiVendor(vendor).XkiStatus(xkiStatus).XkiMessage(xkiMessage).build();
    }

    @Override
    public GenRandomResponse random(GenRandomRequest request) {
        byte[] randomBytes = KeyUtils.genRandom(request.getLength());
        return GenRandomResponse.builder().OutputForm(request.getOutputForm()).Output(Base64Utils.encodeToString(randomBytes)).build();
    }

}
