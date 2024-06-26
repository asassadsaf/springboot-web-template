package com.fkp.template.modules.dbintegrity.params.request;

import com.fkp.template.core.dto.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/19 15:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DatabaseIntegrityPageRequest extends PageRequest {
    private String dbType;
    private String dbIp;
    private String dbCase;
    private String dbSchema;
    private String dbTable;
    private String keyName;
}
