package com.fkp.template.core.util;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/11/20 14:34
 */
public class ByteArrayUtils {
    private ByteArrayUtils(){}

    public static byte[] combine(byte[]... bytes){
        if(bytes == null){
            return new byte[0];
        }
        int len = 0;
        for (byte[] byteArray : bytes) {
            len += byteArray.length;
        }
        byte[] res = new byte[len];
        int index = 0;
        for (byte[] byteArray : bytes) {
            System.arraycopy(byteArray, 0, res, index, byteArray.length);
            index += byteArray.length;
        }
        return res;
    }
}
