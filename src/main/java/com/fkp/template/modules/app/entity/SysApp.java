package com.fkp.template.modules.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author fengkunpeng
 * @since 2024-10-14
 */
@Data
@TableName("sys_app")
@ApiModel(value = "SysApp对象", description = "")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysApp implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField("name")
    private String name;

    private Integer age;

    private String addr;

    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;
}
