package com.fkp.template.listener;

import com.fkp.template.cache.PolicyCache;
import com.fkp.template.service.SystemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 策略文件监听器
 * @date 2024/5/28 17:16
 */
@Component
@Slf4j
public class PolicyFileAlterationListener extends FileAlterationListenerAdaptor {

    @Autowired
    private SystemService systemService;

    @Autowired
    private PolicyCache policyCache;

    @Override
    public void onFileChange(File file) {
        readFile2Cache();
    }

    private void readFile2Cache(){
        try {
            Map<String, Object> policyMap = systemService.queryPolicy().getData();
            for (Map.Entry<String, Object> entry : Optional.ofNullable(policyMap).orElse(Collections.emptyMap()).entrySet()) {
                Object value = entry.getValue();
                if(value instanceof String){
                    String strValue = (String) value;
                    if("true".equalsIgnoreCase(strValue) || "false".equalsIgnoreCase(strValue)){
                        value = Boolean.valueOf(strValue);
                    }
                }
                policyCache.put(entry.getKey(), value);
            }
        }catch (Exception e){
            log.error("Policy file alteration listener read file to map error.", e);
        }
    }
}
