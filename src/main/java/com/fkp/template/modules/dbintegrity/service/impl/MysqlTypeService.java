package com.fkp.template.modules.dbintegrity.service.impl;

import com.fkp.template.modules.dbintegrity.service.DatabaseTypeService;
import org.springframework.stereotype.Service;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/18 16:39
 */
@Service
public class MysqlTypeService implements DatabaseTypeService {
    private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";


    @Override
    public String generateUrl(String ip, Integer port, String dbCase, String dbSchema) {
        return "jdbc:mysql://" + ip + ":" + port + "/" + dbCase;
    }

    @Override
    public String generateDriverClassName() {
        return DRIVER_CLASS_NAME;
    }

    @Override
    public String generateSql(String dbSql, Integer startLimitIndex, Integer limitCount) {
        return dbSql + " limit " + startLimitIndex + "," + limitCount;
    }
}
