package com.fkp.template.modules.dbintegrity.controller;

import com.fkp.template.core.constant.CommonConstant;
import com.fkp.template.core.constant.RestErrorEnum;
import com.fkp.template.core.dto.PageDTO;
import com.fkp.template.core.dto.RestSimpleResponse;
import com.fkp.template.core.validator.order.OrderSequence;
import com.fkp.template.modules.dbintegrity.entity.DatabaseIntegrity;
import com.fkp.template.modules.dbintegrity.params.request.DatabaseIntegrityAddRequest;
import com.fkp.template.modules.dbintegrity.params.request.DatabaseIntegrityPageRequest;
import com.fkp.template.modules.dbintegrity.service.DatabaseIntegrityService;
import com.fkp.template.modules.dbintegrity.util.DatabaseIntegrityValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;


/**
 * @author fengkunpeng
 * @version 1.0
 * @description 数据库完整性接口控制器
 * @date 2024/6/18 10:50
 */
@RestController
@RequestMapping(value = CommonConstant.BASE_URL + "/" + CommonConstant.VERSION_V1 + "/integrity/database", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class DatabaseIntegrityController {

    @Autowired
    private DatabaseIntegrityService databaseIntegrityService;

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestSimpleResponse<?> add(@RequestBody @Validated(OrderSequence.class) DatabaseIntegrityAddRequest params){
        DatabaseIntegrityValidateUtils.dbType(params.getDbType());
        DatabaseIntegrityValidateUtils.dbCaseSchema(params.getDbCase(), params.getDbSchema());
        params.setDbSql(DatabaseIntegrityValidateUtils.dbSql(params.getDbSql()));
        DatabaseIntegrityValidateUtils.startAndEndLimit( params.getStartLimit(), params.getEndLimit());
        return databaseIntegrityService.addIntegrity(params);
    }

    @GetMapping(value = "/delete")
    public RestSimpleResponse<?> delete(@NotBlank(message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_ID_NOT_BLANK_CODE) String id){
        return databaseIntegrityService.deleteIntegrity(id);
    }

    @GetMapping(value = "/verify")
    public RestSimpleResponse<?> verify(@NotBlank(message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_ID_NOT_BLANK_CODE) String id){
        return databaseIntegrityService.verify(id);
    }

    @GetMapping(value = "/refresh")
    public RestSimpleResponse<?> refresh(@NotBlank(message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_ID_NOT_BLANK_CODE) String id){
        return databaseIntegrityService.refresh(id);
    }

    @GetMapping(value = "/update")
    public RestSimpleResponse<?> update(@NotBlank(message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_ID_NOT_BLANK_CODE) String id){
        return databaseIntegrityService.update(id);
    }

    @PostMapping(value = "/page", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestSimpleResponse<PageDTO<DatabaseIntegrity>> page(@RequestBody DatabaseIntegrityPageRequest params){
        DatabaseIntegrityValidateUtils.page(params.getPageNum(), params.getPageSize());
        return databaseIntegrityService.page(params);
    }

}
