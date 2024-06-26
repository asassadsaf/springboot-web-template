package com.fkp.template.modules.dbintegrity.util;

import org.jetbrains.annotations.NotNull;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/18 14:20
 */
public class ParamsConvertUtils {
    private ParamsConvertUtils(){}

    public static Integer convertStartLimitIndex(@NotNull Integer startLimit){
        return startLimit - 1;
    }

    public static Integer convertLimitCount(@NotNull Integer startLimit, @NotNull Integer endLimit){
        return endLimit - startLimit + 1;
    }

    public static Integer convertRealEndLimit(@NotNull Integer endLimit, @NotNull Long rowCount, @NotNull Integer startLimit){
        Integer limitCount = convertLimitCount(startLimit, endLimit);
        if(rowCount < limitCount){
            return rowCount.intValue() + startLimit - 1;
        }
        return limitCount + startLimit - 1;
    }

    public static String convertKeyAlg(@NotNull String keyAlg){
        return keyAlg.replaceAll("_", "");
    }
}
