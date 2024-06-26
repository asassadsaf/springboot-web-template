package com.fkp.template.modules.dbintegrity.util;

import com.fkp.template.core.constant.RestErrorEnum;
import com.fkp.template.core.exception.RestBusinessException;
import com.fkp.template.modules.dbintegrity.constant.DatabaseTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/18 11:03
 */
@Slf4j
public class DatabaseIntegrityValidateUtils {
    private DatabaseIntegrityValidateUtils(){}

    private static final Pattern SQL_PATTERN = Pattern.compile("(?i)\\s*select\\s+.*\\s+from\\s+.*(?:\\s+where\\s+.*)?(?:\\s+group\\s+by\\s+.*)?(?:\\s+having\\s+.*)?(?:\\s+order\\s+by\\s+.*)?\\s*;?");

    public static void dbType(String dbType){
        if (DatabaseTypeEnum.of(dbType) == null) {
            log.error("Parameter dbType validate error. dbType: {}", dbType);
            throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_DB_TYPE_NOT_SUPPORT);
        }
    }

    public static String dbSql(String dbSql){
        if(StringUtils.isBlank(dbSql)){
            log.error("Parameter dbSql validate error. dbSql: {}", dbSql);
            throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_DB_SQL_FORMAT_ERROR);
        }
        if(dbSql.contains(";")){
            dbSql = dbSql.split(";")[0];
        }
        boolean matches = SQL_PATTERN.matcher(dbSql).matches();
        if(!matches){
            log.error("Parameter dbSql validate error. dbSql: {}", dbSql);
            throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_DB_SQL_FORMAT_ERROR);
        }
        return dbSql;
    }

    public static void startAndEndLimit(Integer startLimit, Integer endLimit){
        if(startLimit == null || endLimit == null){
            log.error("Parameter startLimit or endLimit validate error. startLimit: {}, endLimit: {}", startLimit, endLimit);
            throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_DB_START_OR_END_LIMIT_FORMAT_ERROR);
        }
        // limit∈[1,10000], startLimit <= endLimit
        if(startLimit <= 0 || startLimit > 10000 || endLimit <= 0 || endLimit > 10000 || startLimit > endLimit){
            log.error("Parameter startLimit or endLimit validate error. startLimit: {}, endLimit: {}", startLimit, endLimit);
            throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_DB_START_OR_END_LIMIT_FORMAT_ERROR);
        }
    }

    public static void dbCaseSchema(String dbCase, String dbSchema) {
        if(StringUtils.isBlank(dbCase) && StringUtils.isBlank(dbSchema)){
            log.error("Validate param dbCase and dbSchema error, dbCase and dbSchema can not all blank.");
            throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_DB_CASE_SCHEMA_ALL_BLANK_ERROR);
        }
    }

    public static void page(Integer pageNum, Integer pageSize){
        if (pageNum == null || pageNum < 1
                || pageSize == null || pageSize < 1 || pageSize > 100) {
            log.error("Validate page param error. pageSize: {}, pageNum: {}", pageSize, pageNum);
            throw new RestBusinessException(RestErrorEnum.DATABASE_INTEGRITY_PAGE_PARAMS_INVALID);
        }
    }
}
