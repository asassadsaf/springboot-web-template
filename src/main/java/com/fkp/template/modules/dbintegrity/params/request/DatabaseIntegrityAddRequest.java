package com.fkp.template.modules.dbintegrity.params.request;

import com.fkp.template.core.constant.RegexConstant;
import com.fkp.template.core.constant.RestErrorEnum;
import com.fkp.template.core.validator.order.OrderSequence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/18 10:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseIntegrityAddRequest {

    private String dbType;

    @Pattern(regexp = RegexConstant.IP_REGEX, message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_DB_IP_FORMAT_ERROR_CODE, groups = OrderSequence.OrderA.class)
    private String dbIp;

    @NotNull(message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_DB_PORT_FORMAT_ERROR_CODE, groups = OrderSequence.OrderB.class)
    @Range(min = 1, max = 65535, message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_DB_PORT_FORMAT_ERROR_CODE, groups = OrderSequence.OrderB.class)
    private Integer dbPort;

    @NotBlank(message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_DB_USER_FORMAT_ERROR_CODE, groups = OrderSequence.OrderC.class)
    @Size(max = 64, message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_DB_USER_FORMAT_ERROR_CODE, groups = OrderSequence.OrderC.class)
    private String dbUser;

    @NotBlank(message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_DB_PWD_FORMAT_ERROR_CODE, groups = OrderSequence.OrderD.class)
    @Size(max = 128, message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_DB_PWD_FORMAT_ERROR_CODE, groups = OrderSequence.OrderD.class)
    private String dbPwd;

    @Size(max = 64, message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_DB_CASE_FORMAT_ERROR_CODE, groups = OrderSequence.OrderE.class)
    private String dbCase;

    @Size(max = 64, message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_DB_SCHEMA_FORMAT_ERROR_CODE, groups = OrderSequence.OrderF.class)
    private String dbSchema;

    @NotBlank(message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_DB_TABLE_FORMAT_ERROR_CODE, groups = OrderSequence.OrderH.class)
    @Size(max = 64, message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_DB_TABLE_FORMAT_ERROR_CODE, groups = OrderSequence.OrderH.class)
    private String dbTable;

    private String dbSql;

    private Integer startLimit;

    private Integer endLimit;

    @NotNull(message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_DB_FREQUENCY_FORMAT_ERROR_CODE, groups = OrderSequence.OrderI.class)
    @Range(min = 1, max = 60 * 24 * 30, message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_DB_FREQUENCY_FORMAT_ERROR_CODE, groups = OrderSequence.OrderI.class)
    private Integer frequency;

    @NotBlank(message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_KEY_NAME_NOT_BLANK_CODE, groups = OrderSequence.OrderG.class)
    private String keyName;

    @NotBlank(message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_KEY_ALG_NOT_BLANK_CODE, groups = OrderSequence.OrderK.class)
    private String keyAlg;

    @Size(max = 255, message = RestErrorEnum.CodeConstant.DATABASE_INTEGRITY_REMARK_FORMAT_ERROR_CODE, groups = OrderSequence.OrderL.class)
    private String remark;
}
