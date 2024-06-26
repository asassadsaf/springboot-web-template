package com.fkp.template;

import com.fkp.template.core.util.SynthesisCryptoUtils;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/26 17:52
 */
public class CryptoTest {

    @BeforeAll
    static void init(){
        Security.addProvider(new BouncyCastleProvider());
    }

    @SneakyThrows
    @Test
    void testSm4(){
        byte[] plainBlob = "SWXA1234@DAR_Mysql".getBytes();
        byte[] keyBlob = new byte[]{18, -10, 34, -95, -58, 90, 33, -97, 57, 44, 82, 115, 11, -62, 116, -78};
        byte[] iv = new byte[16];
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Key keySpec = new SecretKeySpec(keyBlob, "SM4");
        Cipher cipher = Cipher.getInstance("SM4/CBC/PKCS5Padding", BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] bytes = cipher.doFinal(plainBlob);
        System.out.println(bytes.length);
        System.out.println(Base64.encodeBase64String(bytes));
    }

    @SneakyThrows
    @Test
    void testHmacSm3(){
        byte[] keyBlob = Base64.decodeBase64("jGL0p/PXATUaTlmm+yrT8/hkeqMi1OVyT8Ab9gFb+Q4=");
        byte[] plainBlob = "SWXA1234@DAR_Mwqewqewqeysql".getBytes();
        Mac mac = Mac.getInstance("HMACSM3", BouncyCastleProvider.PROVIDER_NAME);
        Key keySpec = new SecretKeySpec(keyBlob, "HMACSM3");
        mac.init(keySpec);
        byte[] bytes = mac.doFinal(plainBlob);
        System.out.println(bytes.length);
        System.out.println(Base64.encodeBase64String(bytes));
        System.out.println(Hex.encodeHexString(bytes).length());
        int length = "3b9e2eb39b854db61ba5238cac261c2201d1acd446753af6a14fb35bf5284664".length();
        System.out.println(length);
    }


}
