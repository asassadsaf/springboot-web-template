package com.fkp.template.modules.dbintegrity.context;

import com.fkp.template.modules.dbintegrity.service.DatabaseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/18 16:41
 */
@Component
public class DatabaseTypeContext {

    @Autowired
    private Map<String, DatabaseTypeService> serviceMap;

    public DatabaseTypeService of(String type){
        String lowerType = type.toLowerCase(Locale.ROOT);
        DatabaseTypeService databaseTypeService = serviceMap.get(lowerType + "TypeService");
        if(databaseTypeService == null){
            throw new IllegalArgumentException("Database type param error. type: " + type);
        }
        return databaseTypeService;
    }

}
