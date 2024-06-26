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
 * @description
 * @date 2024/6/26 18:41
 */
public class CryptoUtils {
    private CryptoUtils(){}

    public static byte[] sm4EncDec(int mode, byte[] keyBlob, byte[] plainBlob) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        byte[] iv = new byte[16];
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Key keySpec = new SecretKeySpec(keyBlob, "SM4");
        Cipher cipher = Cipher.getInstance("SM4/CBC/PKCS5Padding", BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(mode, keySpec, ivSpec);
        return cipher.doFinal(plainBlob);
    }

    public static byte[] hmacSm3(byte[] keyBlob, byte[] plainBlob) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        Mac mac = Mac.getInstance("HMACSM3", BouncyCastleProvider.PROVIDER_NAME);
        Key keySpec = new SecretKeySpec(keyBlob, "HMACSM3");
        mac.init(keySpec);
        return mac.doFinal(plainBlob);
    }
}
