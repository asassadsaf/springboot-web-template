package com.fkp.template.core.config.database.typehandler;

import org.apache.ibatis.type.DateTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description MybatisPlus 类型转换器，用于在java实体和数据库字段间进行类型转换，日期转换类，JAVA类型为Date时，将其转换为指定格式的字符串写入statement
 * @date 2024/6/20 21:39
 */

public class MyDateTypeHandler extends DateTypeHandler {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateFormat = format.format(parameter);
        ps.setString(i, dateFormat);
    }
}
