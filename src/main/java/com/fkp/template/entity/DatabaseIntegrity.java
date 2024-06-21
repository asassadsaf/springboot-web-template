package com.fkp.template.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author fengkunpeng
 * @since 2024-06-18
 */
@Data
@TableName("storage_database_integrity")
public class DatabaseIntegrity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String dbType;

    private String dbIp;

    private Integer dbPort;

    private String dbUser;

    @JsonIgnore
    private String dbPwd;

    private String dbCase;

    private String dbSchema;

    private String dbTable;

    private String dbSql;

    private Integer startLimit;

    private Integer endLimit;

    private Integer frequency;

    private String keyId;

    private String keyAlg;

    private String keyName;

    private Integer verifyStatus;

    private String hmacHex;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC+8")
    private Date createDate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC+8")
    private Date updateDate;

    private String remark;

}
