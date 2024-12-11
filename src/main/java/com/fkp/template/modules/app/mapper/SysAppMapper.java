package com.fkp.template.modules.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fkp.template.modules.app.entity.SysApp;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fengkunpeng
 * @since 2024-10-14
 */
public interface SysAppMapper extends BaseMapper<SysApp> {

    List<Map<String, Object>> selectTest(@Param("current") String current);
}
