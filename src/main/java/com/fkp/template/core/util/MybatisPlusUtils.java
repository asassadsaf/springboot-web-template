package com.fkp.template.core.util;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.Map;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2023/9/15 19:28
 */

public class MybatisPlusUtils {

    /**
     * 通过方法引用获取该字段对应的数据库字段名
     * @param func
     * @return
     * @param <T>
     */
    public static <T> String getColumnNameByRef(SFunction<T, ?> func) {
        LambdaMeta lambdaMeta = LambdaUtils.extract(func);
        Map<String, ColumnCache> columnMap = LambdaUtils.getColumnMap(lambdaMeta.getInstantiatedClass());
        String fieldName = PropertyNamer.methodToProperty(lambdaMeta.getImplMethodName());
        return columnMap.get(LambdaUtils.formatKey(fieldName)).getColumn();
    }
}
