package com.fkp.template;


import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.AlphabetMapper;
import org.bouncycastle.crypto.util.BasicAlphabetMapper;
import org.bouncycastle.jcajce.spec.FPEParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/8/29 16:09
 */
@Slf4j
public class FpeTest {

//    @Test
//    public void testAesFpe() throws Exception {
//        SecretKey key = generateKey();
//        byte[] tweak = getTweak();
//        int radix = getRadix("0123456789");
//        Charset encoding = StandardCharsets.UTF_8;
//        byte[] plaintext = "510123456".getBytes(encoding);
//        Cipher cipher = Cipher.getInstance("AES/FF3-1/NoPadding", new BouncyCastleProvider());
//        byte[] ciphertext = encrypt(cipher, key, tweak, radix, plaintext);
//        log.info("Ciphertext: {}", new String(ciphertext));
//        byte[] decrypted = decrypt(cipher, key, tweak, radix, ciphertext);
//        assertThat(decrypted, equalTo(plaintext));
//    }

    @Test
    public void testAesFpe2() throws Exception {
        SecretKey key = generateKey();
        byte[] tweak = getTweak();
        AlphabetMapper alphabetMapper = new BasicAlphabetMapper("0123456789-");
        int radix = alphabetMapper.getRadix();
        char[] plaintext = "123456".toCharArray();

        byte[] plain_bytes = alphabetMapper.convertToIndexes(plaintext);

        Cipher cipher = Cipher.getInstance("AES/FF1/NoPadding", new BouncyCastleProvider());
        byte[] cipher_bytes = encrypt(cipher, key, tweak, radix, plain_bytes);

        byte[] decrypted = decrypt(cipher, key, tweak, radix, cipher_bytes);
        char[] cipher_chars = alphabetMapper.convertToChars(cipher_bytes);
        System.out.println(new String(cipher_chars));

        char[] plain_chars = alphabetMapper.convertToChars(decrypted);
        System.out.println(new String(plain_chars));
    }

    public byte[] encrypt(Cipher cipher, SecretKey key, byte[] tweak, int radix, byte[] plaintext) throws Exception {
        AlgorithmParameterSpec fpeParameterSpec = new FPEParameterSpec(radix, tweak);
        cipher.init(Cipher.ENCRYPT_MODE, key, fpeParameterSpec);
        return cipher.doFinal(plaintext);
    }

    public byte[] decrypt(Cipher cipher, SecretKey key, byte[] tweak, int radix, byte[] ciphertext) throws Exception {
        AlgorithmParameterSpec fpeParameterSpec = new FPEParameterSpec(radix, tweak);
        cipher.init(Cipher.DECRYPT_MODE, key, fpeParameterSpec);
        return cipher.doFinal(ciphertext);
    }

    private SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        int keyLength = 256;
        keyGenerator.init(keyLength);
        return keyGenerator.generateKey();
    }

    private byte[] getTweak() {
        int tweakLength = 7;
        byte[] tweak = new byte[tweakLength];
//        new SecureRandom().nextBytes(tweak);
        return tweak;
    }

    private int getRadix(String alphabet) {
        AlphabetMapper alphabetMapper = new BasicAlphabetMapper(alphabet);
        int radix = alphabetMapper.getRadix();
        log.info("Radix: {}", radix);
        return radix;
    }
}
