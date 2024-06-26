package com.fkp.template.core.config.database;

import com.fkp.template.core.constant.DatabaseTypeEnum;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;


import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 自定义DatabaseId实现
 * @date 2024/6/20 21:39
 */

@Setter
public class MyVendorDatabaseIdProvider extends VendorDatabaseIdProvider {

    private String databaseType;

    public MyVendorDatabaseIdProvider() {
        Properties prop = new Properties();
        for (DatabaseTypeEnum value : DatabaseTypeEnum.values()) {
            prop.setProperty(value.name(), value.name());
        }
        this.setProperties(prop);
    }

    @Override
    public String getDatabaseId(DataSource dataSource) {
        if(StringUtils.isBlank(databaseType)){
            return super.getDatabaseId(dataSource);
        }
        return databaseType;
    }

}
