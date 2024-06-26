package com.fkp.template.modules.dbintegrity.service;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/18 16:39
 */
public interface DatabaseTypeService {

    String generateUrl(String ip, Integer port, String dbCase, String dbSchema);

    String generateDriverClassName();

    String generateSql(String dbSql, Integer startLimitIndex, Integer limitCount);
}
