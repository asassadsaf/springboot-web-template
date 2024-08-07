package com.fkp.template.core.constant;

import lombok.Getter;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description Rest接口错误码枚举
 * @date 2024/6/26 13:38
 */
@Getter
public enum RestErrorEnum {
    DATABASE_EXCEPTION(CodeConstant.DATABASE_EXCEPTION_CODE, "Database Error."),
    CRYPTO_DEVICE_ERROR(CodeConstant.CRYPTO_DEVICE_ERROR_CODE, "Crypto device error"),
    AUTHENTICATION_ERROR(CodeConstant.AUTHENTICATION_ERROR_CODE, "Authentication error."),
    AUTHORIZATION_ERROR(CodeConstant.AUTHORIZATION_ERROR_CODE, "Authentication failed, please login."),
    PARAMS_INVALID(CodeConstant.PARAMS_INVALID_CODE, "Parameter format error"),
    INTERNAL_SERVER_ERROR(CodeConstant.INTERNAL_SERVER_ERROR_CODE, "Internal server error."),

    DATABASE_INTEGRITY_DB_TYPE_NOT_SUPPORT(CodeConstant.DATABASE_INTEGRITY_DB_TYPE_NOT_SUPPORT_CODE, "Database type not support."),
    DATABASE_INTEGRITY_DB_IP_FORMAT_ERROR(CodeConstant.DATABASE_INTEGRITY_DB_IP_FORMAT_ERROR_CODE, "Database ip format error."),
    DATABASE_INTEGRITY_DB_PORT_FORMAT_ERROR(CodeConstant.DATABASE_INTEGRITY_DB_PORT_FORMAT_ERROR_CODE, "Database port format error."),
    DATABASE_INTEGRITY_DB_USER_FORMAT_ERROR(CodeConstant.DATABASE_INTEGRITY_DB_USER_FORMAT_ERROR_CODE, "Database user format error."),
    DATABASE_INTEGRITY_DB_PWD_FORMAT_ERROR(CodeConstant.DATABASE_INTEGRITY_DB_PWD_FORMAT_ERROR_CODE, "Database password format error."),
    DATABASE_INTEGRITY_DB_CASE_FORMAT_ERROR(CodeConstant.DATABASE_INTEGRITY_DB_CASE_FORMAT_ERROR_CODE, "Database case format error."),
    DATABASE_INTEGRITY_DB_SCHEMA_FORMAT_ERROR(CodeConstant.DATABASE_INTEGRITY_DB_SCHEMA_FORMAT_ERROR_CODE, "Database schema format error."),
    DATABASE_INTEGRITY_DB_TABLE_FORMAT_ERROR(CodeConstant.DATABASE_INTEGRITY_DB_TABLE_FORMAT_ERROR_CODE, "Database table format error."),
    DATABASE_INTEGRITY_REMARK_FORMAT_ERROR(CodeConstant.DATABASE_INTEGRITY_REMARK_FORMAT_ERROR_CODE, "Remark format error."),
    DATABASE_INTEGRITY_KEY_NAME_NOT_BLANK(CodeConstant.DATABASE_INTEGRITY_KEY_NAME_NOT_BLANK_CODE, "Key name is not blank."),
    DATABASE_INTEGRITY_KEY_ALG_NOT_BLANK(CodeConstant.DATABASE_INTEGRITY_KEY_ALG_NOT_BLANK_CODE, "Key alg is not blank."),
    DATABASE_INTEGRITY_DB_SQL_FORMAT_ERROR(CodeConstant.DATABASE_INTEGRITY_DB_SQL_FORMAT_ERROR_CODE, "Select sql format error."),
    DATABASE_INTEGRITY_DB_START_OR_END_LIMIT_FORMAT_ERROR(CodeConstant.DATABASE_INTEGRITY_DB_START_OR_END_LIMIT_FORMAT_ERROR_CODE, "Limit count format error."),
    DATABASE_INTEGRITY_DB_FREQUENCY_FORMAT_ERROR(CodeConstant.DATABASE_INTEGRITY_DB_FREQUENCY_FORMAT_ERROR_CODE, "Frequency format error."),
    DATABASE_INTEGRITY_CONNECT_REMOTE_DB_ERROR(CodeConstant.DATABASE_INTEGRITY_CONNECT_REMOTE_DB_ERROR_CODE, "Connection to remote database failed."),
    DATABASE_INTEGRITY_EXECUTE_SQL_ERROR(CodeConstant.DATABASE_INTEGRITY_EXECUTE_SQL_ERROR_CODE, "Execute sql error."),
    DATABASE_INTEGRITY_EXECUTE_SQL_NO_DATA_ERROR(CodeConstant.DATABASE_INTEGRITY_EXECUTE_SQL_NO_DATA_ERROR_CODE, "Select data is blank."),
    DATABASE_INTEGRITY_DO_HMAC_ERROR(CodeConstant.DATABASE_INTEGRITY_DO_HMAC_ERROR_CODE, "Generate Hmac error."),
    DATABASE_INTEGRITY_KEY_ALG_NOT_MATCH_ERROR(CodeConstant.DATABASE_INTEGRITY_KEY_ALG_NOT_MATCH_ERROR_CODE, "Key Algorithm not match."),
    DATABASE_INTEGRITY_DB_CASE_SCHEMA_ALL_BLANK_ERROR(CodeConstant.DATABASE_INTEGRITY_DB_CASE_SCHEMA_ALL_BLANK_ERROR_CODE, "Database case and schema can not all blank."),
    DATABASE_INTEGRITY_DB_INFO_ALREADY_EXIST_ERROR(CodeConstant.DATABASE_INTEGRITY_DB_INFO_ALREADY_EXIST_ERROR_CODE, "Database integrity information already exists."),
    DATABASE_INTEGRITY_ID_NOT_BLANK(CodeConstant.DATABASE_INTEGRITY_ID_NOT_BLANK_CODE, "Database integrity id not blank."),
    DATABASE_INTEGRITY_DB_INFO_NOT_EXIST_ERROR(CodeConstant.DATABASE_INTEGRITY_DB_INFO_NOT_EXIST_ERROR_CODE, "Database integrity information not exists."),
    DATABASE_INTEGRITY_PAGE_PARAMS_INVALID(CodeConstant.DATABASE_INTEGRITY_PAGE_PARAMS_INVALID_CODE, "Query page params format error.");

    private final String code;
    private final String msg;

    RestErrorEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    //枚举类错误码的常量类，以便在注解属性中使用
    public static class CodeConstant {
        public static final String DATABASE_EXCEPTION_CODE = "05000020";
        public static final String CRYPTO_DEVICE_ERROR_CODE = "05000030";
        public static final String AUTHENTICATION_ERROR_CODE = "05000040";
        public static final String AUTHORIZATION_ERROR_CODE = "05000050";
        public static final String PARAMS_INVALID_CODE = "05000060";
        public static final String INTERNAL_SERVER_ERROR_CODE = "05000000";
        public static final String DATABASE_INTEGRITY_DB_TYPE_NOT_SUPPORT_CODE = "05235001";
        public static final String DATABASE_INTEGRITY_DB_IP_FORMAT_ERROR_CODE = "05235002";
        public static final String DATABASE_INTEGRITY_DB_PORT_FORMAT_ERROR_CODE = "05235003";
        public static final String DATABASE_INTEGRITY_DB_USER_FORMAT_ERROR_CODE = "05235004";
        public static final String DATABASE_INTEGRITY_DB_PWD_FORMAT_ERROR_CODE = "05235005";
        public static final String DATABASE_INTEGRITY_DB_CASE_FORMAT_ERROR_CODE = "05235006";
        public static final String DATABASE_INTEGRITY_DB_SCHEMA_FORMAT_ERROR_CODE = "05235007";
        public static final String DATABASE_INTEGRITY_DB_TABLE_FORMAT_ERROR_CODE = "05235008";
        public static final String DATABASE_INTEGRITY_REMARK_FORMAT_ERROR_CODE = "05235009";
        public static final String DATABASE_INTEGRITY_KEY_NAME_NOT_BLANK_CODE = "05235010";
        public static final String DATABASE_INTEGRITY_KEY_ALG_NOT_BLANK_CODE = "05235011";
        public static final String DATABASE_INTEGRITY_DB_SQL_FORMAT_ERROR_CODE = "05235012";
        public static final String DATABASE_INTEGRITY_DB_START_OR_END_LIMIT_FORMAT_ERROR_CODE = "05235013";
        public static final String DATABASE_INTEGRITY_DB_FREQUENCY_FORMAT_ERROR_CODE = "05235014";
        public static final String DATABASE_INTEGRITY_CONNECT_REMOTE_DB_ERROR_CODE = "05235015";
        public static final String DATABASE_INTEGRITY_EXECUTE_SQL_ERROR_CODE = "05235016";
        public static final String DATABASE_INTEGRITY_EXECUTE_SQL_NO_DATA_ERROR_CODE = "05235017";
        public static final String DATABASE_INTEGRITY_DO_HMAC_ERROR_CODE = "05235018";
        public static final String DATABASE_INTEGRITY_KEY_ALG_NOT_MATCH_ERROR_CODE = "05235019";
        public static final String DATABASE_INTEGRITY_DB_CASE_SCHEMA_ALL_BLANK_ERROR_CODE = "05235020";
        public static final String DATABASE_INTEGRITY_DB_INFO_ALREADY_EXIST_ERROR_CODE = "05235021";
        public static final String DATABASE_INTEGRITY_ID_NOT_BLANK_CODE = "05235022";
        public static final String DATABASE_INTEGRITY_DB_INFO_NOT_EXIST_ERROR_CODE = "05235023";
        public static final String DATABASE_INTEGRITY_PAGE_PARAMS_INVALID_CODE = "05235024";
    }

    public static RestErrorEnum getByCode(String code){
        for (RestErrorEnum value : RestErrorEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
