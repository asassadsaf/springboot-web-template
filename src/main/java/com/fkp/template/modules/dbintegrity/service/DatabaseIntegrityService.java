package com.fkp.template.modules.dbintegrity.service;

import com.fkp.template.core.dto.PageDTO;
import com.fkp.template.core.dto.RestSimpleResponse;
import com.fkp.template.modules.dbintegrity.constant.VerifyStatusEnum;
import com.fkp.template.modules.dbintegrity.entity.DatabaseIntegrity;
import com.fkp.template.modules.dbintegrity.params.request.DatabaseIntegrityAddRequest;
import com.fkp.template.modules.dbintegrity.params.request.DatabaseIntegrityPageRequest;

import java.util.List;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/18 14:37
 */
public interface DatabaseIntegrityService {
    RestSimpleResponse<?> addIntegrity(DatabaseIntegrityAddRequest params);
    RestSimpleResponse<?> deleteIntegrity(String id);
    RestSimpleResponse<?> verify(String id);
    RestSimpleResponse<?> refresh(String id);
    RestSimpleResponse<?> update(String id);
    RestSimpleResponse<PageDTO<DatabaseIntegrity>> page(DatabaseIntegrityPageRequest params);

    Long queryCountByUnique(DatabaseIntegrity params);
    void save(DatabaseIntegrity databaseIntegrity);
    void updateVerifyStatusById(String id, VerifyStatusEnum verifyStatusEnum);
    List<DatabaseIntegrity> listIdAndFrequency();
    void startVerifyIntegrityScheduler(String id, Integer frequency);



}
