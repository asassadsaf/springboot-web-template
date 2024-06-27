package com.fkp.template.core.util;

import com.fkp.template.core.exception.RestBusinessException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 密码运算工具类
 * @date 2024/6/26 18:41
 */
public class CryptoUtils {
    private CryptoUtils(){}

    /**
     * SM4/CBC/PKCS5Padding加解密
     * @param mode 加密1/解密2
     * @param keyBlob 密钥
     * @param plainBlob 明文或密文
     * @return 密文或明文
     */
    public static byte[] sm4EncDec(int mode, byte[] keyBlob, byte[] plainBlob) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        byte[] iv = new byte[16];
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Key keySpec = new SecretKeySpec(keyBlob, "SM4");
        Cipher cipher = Cipher.getInstance("SM4/CBC/PKCS5Padding", BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(mode, keySpec, ivSpec);
        return cipher.doFinal(plainBlob);
    }

    /**
     * HMAC_SM3运算
     * @param keyBlob SM3密钥
     * @param plainBlob 待HMAC数据
     * @return HMAC后的数据
     */
    public static byte[] hmacSm3(byte[] keyBlob, byte[] plainBlob) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        Mac mac = Mac.getInstance("HMACSM3", BouncyCastleProvider.PROVIDER_NAME);
        Key keySpec = new SecretKeySpec(keyBlob, "HMACSM3");
        mac.init(keySpec);
        return mac.doFinal(plainBlob);
    }
}
