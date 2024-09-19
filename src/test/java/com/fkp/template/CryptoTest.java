package com.fkp.template;

import cn.hutool.crypto.symmetric.fpe.FPE;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.fpe.FPEFF1Engine;
import org.bouncycastle.crypto.params.FPEParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.util.BasicAlphabetMapper;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
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
        byte[] plainBlob = "SWXA1234@DAR_Mwqewdsfsddfsqewqeysql".getBytes();
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

    @SneakyThrows
    @Test
    void testHutoolsFpe(){
        byte[] key = Hex.decodeHex("2B7E151628AED2A6ABF7158809CF4F3C");
        FPE fpe = new FPE(FPE.FPEMode.FF3_1, key, new BasicAlphabetMapper("0123456789a"));
        String encrypt = fpe.encrypt("1566273504a5");
        System.out.println(encrypt);

    }

    @SneakyThrows
    @Test
    void testFpe(){
        // 加密密钥 (必须是AES的128位、192位或256位)
        byte[] key = Hex.decodeHex("2B7E151628AED2A6ABF7158809CF4F3C");

        // 待加密的数据 (例如：电话号码)
        String plaintext = "1234567890";

        BasicAlphabetMapper basicAlphabetMapper = new BasicAlphabetMapper("0123456789");
        int radix = basicAlphabetMapper.getRadix();
        System.out.println(radix);



        boolean validForRadix = isValidForRadix(plaintext, radix);
        System.out.println(validForRadix);

        byte[] tweak = new byte[0];

        // 加密
        String ciphertext = encrypt(plaintext, key, radix, tweak);
        System.out.println("Ciphertext: " + ciphertext);

        // 解密
        String decryptedText = decrypt(ciphertext, key, radix);
        System.out.println("Decrypted Text: " + decryptedText);
    }

    public static boolean isValidForRadix(String input, int radix) {
        // 根据radix检查输入是否只包含合法字符
        if (radix <= 10) {
            // 只允许数字
            for (char c : input.toCharArray()) {
                if (c < '0' || c >= ('0' + radix)) {
                    return false;
                }
            }
        } else if (radix <= 36) {
            // 允许数字和字母
            for (char c : input.toCharArray()) {
                if ((c < '0' || c > '9') && (c < 'a' || c >= ('a' + radix - 10))) {
                    return false;
                }
            }
        } else if (radix <= 62) {
            // 允许数字、大小写字母
            for (char c : input.toCharArray()) {
                if ((c < '0' || c > '9') &&
                        (c < 'a' || c > 'z') &&
                        (c < 'A' || c > 'Z')) {
                    return false;
                }
            }
        } else {
            throw new IllegalArgumentException("Unsupported radix: " + radix);
        }
        return true;
    }
    public String encrypt(String plaintext, byte[] key, int radix, byte[] tweak) {
        FPEFF1Engine engine = new FPEFF1Engine();
        KeyParameter keyParam = new KeyParameter(key);
        FPEParameters params = new FPEParameters(keyParam, radix, tweak);

        engine.init(true, params);
        byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] ciphertextBytes = new byte[plaintextBytes.length];

        engine.processBlock(plaintextBytes, 0, plaintextBytes.length, ciphertextBytes, 0);

        return new String(ciphertextBytes, StandardCharsets.UTF_8);
    }

    public String decrypt(String ciphertext, byte[] key, int radix) {
        FPEFF1Engine engine = new FPEFF1Engine();
        KeyParameter keyParam = new KeyParameter(key);
        FPEParameters params = new FPEParameters(keyParam,radix, new byte[0]);

        engine.init(false, params);
        byte[] ciphertextBytes = ciphertext.getBytes(StandardCharsets.UTF_8);
        byte[] plaintextBytes = new byte[ciphertextBytes.length];

        engine.processBlock(ciphertextBytes, 0, ciphertextBytes.length, plaintextBytes, 0);

        return new String(plaintextBytes, StandardCharsets.UTF_8);
    }
}
