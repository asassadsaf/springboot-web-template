package com.fkp.template.modules.xkip.service.impl;

import com.fkp.template.core.constant.CommonConstant;
import com.fkp.template.core.constant.ServerStatusEnum;
import com.fkp.template.modules.xkip.dto.request.GenRandomRequest;
import com.fkp.template.modules.xkip.dto.request.RequestMetadata;
import com.fkp.template.modules.xkip.dto.response.GenRandomResponse;
import com.fkp.template.modules.xkip.dto.response.XkiHealthResponse;
import com.fkp.template.modules.xkip.service.TestService;
import com.fkp.template.core.util.KeyUtils;
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
        String serverStatus = ServerStatusEnum.ACTIVE.name();
        String serverMessage = CommonConstant.OK;
        try {
            //call service
        }catch (Exception e){
            serverStatus = ServerStatusEnum.UNAVAILABLE.name();
            serverMessage = "Call ekm get health status error.";
            log.error(serverMessage, e);
        }
        return XkiHealthResponse.builder().Specification(specification).ServerName(serverName).ServerVersion(serverVersion)
                .Vendor(vendor).Status(serverStatus).Message(serverMessage).build();
    }

    @Override
    public GenRandomResponse random(GenRandomRequest request) {
        byte[] randomBytes = KeyUtils.genRandom(request.getLength());
        return GenRandomResponse.builder().OutputForm(request.getOutputForm()).Output(Base64Utils.encodeToString(randomBytes)).build();
    }

}
