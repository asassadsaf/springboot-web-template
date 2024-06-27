package com.fkp.template.core.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 三分量合成加解密工具类
 * @date 2024/6/26 16:06
 */

public class SynthesisCryptoUtils {


    private static byte[] LMK1;
    private static byte[] LMK2;
    private static byte[] LMK3;
    private static boolean initFlag = false;

    public static void init(String keyStr, String file) throws IOException {
        LMK1 = Base64.getDecoder().decode(keyStr);
        LMK2 = file2byte(file);
        LMK3 = Base64.getDecoder().decode("qtSRA+zF6foUjSTkyeAzkw==");
        initFlag = true;
    }

    public static String decPwd(String text) throws Exception {
        byte[] keyBlob = getKey(LMK1,LMK2,LMK3);
        byte[] decResult = CryptoUtils.sm4EncDec(Cipher.DECRYPT_MODE, keyBlob, org.apache.commons.codec.binary.Base64.decodeBase64(text));
        return new String(decResult, StandardCharsets.UTF_8);
    }

    public static String encPwd(String text) throws Exception {
        byte[] keyBlob = getKey(LMK1,LMK2,LMK3);
        byte[] decResult = CryptoUtils.sm4EncDec(Cipher.ENCRYPT_MODE, keyBlob, text.getBytes(StandardCharsets.UTF_8));
        return org.apache.commons.codec.binary.Base64.encodeBase64String(decResult);
    }


    private static byte[] file2byte(String path) throws IOException {
        File file = new File(path);
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fileInputStream.read(b)) != -1) {
                byteArrayOutputStream.write(b, 0 , n);
            }
            return byteArrayOutputStream.toByteArray();
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static byte[] getKey(byte[]... keys) throws Exception {
        if(!initFlag){
            throw new RuntimeException("SynthesisPwdUtil not init,please init first.");
        }
        byte[] finalKey = new byte[16];
        byte[][] var2 = keys;
        int var3 = keys.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte[] key = var2[var4];
            if (key.length != 16) {
                return null;
            }

            finalKey = getXor(finalKey, key);
        }

        return finalKey;
    }

    public static byte[] getXor(byte[] x, byte[] y) {
        if (x.length != y.length) {
            return null;
        } else {
            byte[] z = new byte[x.length];

            for(int i = 0; i < x.length; ++i) {
                z[i] = (byte)(x[i] ^ y[i]);
            }

            return z;
        }
    }

}
