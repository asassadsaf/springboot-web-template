package com.fkp.template;

import cn.hutool.core.util.ByteUtil;
import cn.hutool.crypto.symmetric.fpe.FPE;
import com.kms.util.crypto.CryptoParameter;
import com.kms.util.crypto.CryptoUtils;
//import com.sansec.jcajce.provider.asymmetric.sm2.JCESM2PublicKey;
//import com.sansec.jce.provider.SwxaProvider;
import com.kms.util.crypto.SynthesisPwdUtil;
import com.kms.util.crypto.exception.CryptoAlgParameterException;
import com.kms.util.crypto.exception.CryptoDataException;
import com.kms.util.crypto.exception.CryptoKeyPraseException;
import com.sansec.jce.provider.SwxaProvider;
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
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/26 17:52
 */
public class CryptoTest {

    @BeforeAll
    static void init(){
//        Security.addProvider(new BouncyCastleProvider());
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

    @SneakyThrows
    @Test
    void testEncDec(){
        Security.addProvider(new SwxaProvider(null, null));
        System.setProperty("SANSEC.SSL", "TRUE");
//        KeyPairGenerator generator;
//        generator = KeyPairGenerator.getInstance("SM2", "SwxaJCE");
//        KeyPair keyPair = generator.generateKeyPair();
//        PublicKey publicKey = keyPair.getPublic();
//        PrivateKey privateKey = keyPair.getPrivate();
//        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
//        String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
//        System.out.println(publicKeyStr);
//        System.out.println(privateKeyStr);
//        String textData = "swxa1234.";
//        String pubKeyStr = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE8yyTHHqI6r+2MfZJqiQa9WFh08s9c01iWJrRHCswrjCleWMMYFf5b8t3w+ztBtXu4y5W6xNC0AY3vzGpmj83gQ==";
//        String cipher2 = CryptoUtils.asyEnc(pubKeyStr, Base64.encodeBase64String(textData.getBytes(StandardCharsets.UTF_8)), new CryptoParameter("SM2"));
//        System.out.println(cipher2);
//        String priKeyStr = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQIgTMdvuY/NNFCetM8Hf4+fxhxJaqJMqDuVE+QvMct/98KgCgYIKoEcz1UBgi2hRANCAATzLJMceojqv7Yx9kmqJBr1YWHTyz1zTWJYmtEcKzCuMKV5YwxgV/lvy3fD7O0G1e7jLlbrE0LQBje/MamaPzeB";
//        String cipher = "BLc2kBPa6PdTAssKk207AUsevSiTJumO6ftnoNgS/OTXJtwwEwDCtW+CYgpBIV61SzPKNIYl58R05mqAW3BOgU9dFc8Eqw49p7cjrG3cawMk8iMSSL7eFTFqeuMO3h/LZBVwB7vTf0y4rw2nDQ==";
//        String plain = CryptoUtils.asyDec(priKeyStr, cipher2, new CryptoParameter("SM2"));
//        System.out.println(plain);
//        System.out.println(new String(Base64.decodeBase64(plain), StandardCharsets.UTF_8));
//        byte[] gmPubKey = Hex.decodeHex("9C2E943A092CFDC319CBAC86C29F7C43A22ADB8B76DD06FAD0D625A623FD028B5213D7E65550AD50BCCCE2613020EB82D91E9DF75A47A4F59AD4E18DE6800732");
//        byte[] bytes = CryptoUtils.convertSM2PublicToAsn1(gmPubKey);
//        System.out.println(Base64.encodeBase64String(bytes));

//        String pubKeyStr = "04820ea64465de6de616686109bcabd69cba7822811a5621afbd72202b72a6914d3fcf02ffbe90282e2774c98cd2814d3bd980a8c473d4e60a2f8ecd17b6e4e231";
//        System.out.println(Arrays.toString(Hex.decodeHex(pubKeyStr)));
//        System.out.println(Base64.encodeBase64String(Hex.decodeHex(pubKeyStr)));
//        System.out.println(Arrays.toString();
//        System.out.println(Base64.decodeBase64("MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE8yyTHHqI6r+2MfZJqiQa9WFh08s9c01iWJrRHCswrjCleWMMYFf5b8t3w+ztBtXu4y5W6xNC0AY3vzGpmj83gQ=="));
//        byte[] key = Hex.decodeHex("9C2E943A092CFDC319CBAC86C29F7C43A22ADB8B76DD06FAD0D625A623FD028B5213D7E65550AD50BCCCE2613020EB82D91E9DF75A47A4F59AD4E18DE6800732");
//        System.out.println(ByteUtil.bytesToInt(Arrays.copyOfRange(key, 0, 4)));

//        String bcPubKeyStr = "3b31359db5bc67dabbdb5ebc39ec4905746a1fa7e70bac0a40dabc7e1a7010870136b01b9e972c6b3ab00c17fdf34bf8fde830865c236f3a71b6b031d7863b86";
//        String bcPriKeyStr = "b7d621fb4b41f9c3c2ca94f4ed99038371b003a75d7e0ed7f5ca7aae8ad9a84e";
//
//        byte[] bytes = CryptoUtils.convertSM2PublicToAsn1(Hex.decodeHex(bcPubKeyStr));
//        byte[] bytes1 = CryptoUtils.convertSM2PrivateToAsn1(Hex.decodeHex(bcPriKeyStr), Hex.decodeHex(bcPubKeyStr));
//        System.out.println(Base64.encodeBase64String(bytes));
//        System.out.println(Base64.encodeBase64String(bytes1));
//
//        System.out.println(Base64.encodeBase64String(CryptoUtils.convertSM2PublicToGM(bytes)));
//        System.out.println(Base64.encodeBase64String(CryptoUtils.convertSM2PrivateToGM(bytes1)));
//
//        System.out.println(Hex.encodeHexString(CryptoUtils.convertSM2PublicToGM(bytes)));
//        System.out.println(Hex.encodeHexString(CryptoUtils.convertSM2PrivateToGM(bytes1)));
//
//        System.out.println(CryptoUtils.convertSM2PublicToGM(bytes).length);
//        System.out.println(CryptoUtils.convertSM2PrivateToGM(bytes1).length);

//        byte[] gmPubKey = CryptoUtils.convertSM2PublicToGM(Base64.decodeBase64(pubKeyStr));
//        byte[] gmPriKey = CryptoUtils.convertSM2PrivateToGM(Base64.decodeBase64(priKeyStr));
//        System.out.println(Arrays.toString(gmPubKey));
//        System.out.println(Arrays.toString(gmPriKey));
//        System.out.println(Hex.encodeHexString(gmPubKey));
//        System.out.println(Hex.encodeHexString(gmPriKey));
//        X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.decodeBase64(pubKeyStr));
//        KeyFactory keyFactory = KeyFactory.getInstance("SM2", "SwxaJCE");
//        JCESM2PublicKey publicKey = (JCESM2PublicKey) keyFactory.generatePublic(spec);
//        System.out.println(publicKey);
//        System.out.println(publicKey.getW().getAffineX().toString(16));
//        System.out.println(publicKey.getW().getAffineY().toString(16));
//        System.out.println();
//        String cipher = "2265afb1b412fa834b86d4049760b25942f0e6a3dc302cd881fdb2ae1fcc89c99370ed22ac09fc5dad5cd0dd84eaf76b5ffaa0018859a2ab028387c66d07e1959d48c92f38c205b28c1c6536a51b6bde839c77bd85d3d3e716d839e7bef05937e49fe5d555d945ad6a";
//        String cipher = "7D12050E36990468C0967DA77DCA6D0F7B2FAA69A1E0A2536E689DA0990962BA0D9ACDDD0B87DF2E83FDCC34562F51DDDE264F53399EDF52DAE2D045BD2152EB084D5EB9FB05D59D605569C843B00327F408797783D4FB2231B694A8D8DEAD574E2C12AB3026C026C4";
//        byte[] bytes = CryptoUtils.convertSM2CipherToASN1(Hex.decodeHex(cipher));
//        byte[] bcCipher = Hex.decodeHex(cipher);
//        byte[] bytes = CryptoUtils.convertSM2CipherToASN1(CryptoUtils.convertBCSM2CipherToGM(bcCipher, bcCipher.length - 96));
//        System.out.println(Base64.encodeBase64String(bytes));
//        String s = CryptoUtils.asyDec(priKeyStr, Base64.encodeBase64String(bytes), new CryptoParameter("SM2"));
//        System.out.println(new String(Base64.decodeBase64(s), StandardCharsets.UTF_8));

        String xyPubKeyStr = "0732039467d29e35cca437bdecbeb9a867301c66e4f7e94e2167270360da9343f77fd3c63d1571d8f601c5505137c1a55deef2d62bc8756e2db14a0a1e1859f0";
        byte[] bytes = CryptoUtils.convertSM2PublicToAsn1(Hex.decodeHex(xyPubKeyStr));
        byte[] bytes1 = CryptoUtils.asyEnc(bytes, "swxa1234.".getBytes(StandardCharsets.UTF_8), new CryptoParameter("SM2"));
        System.out.println(Hex.encodeHexString(CryptoUtils.convertGMSM2CipherToBC(CryptoUtils.convertSM2CipherTo0018(bytes1))));
//        System.out.println(Base64.encodeBase64String(bytes));
    }


    @SneakyThrows
    @Test
    void testHmacSm3_2() throws CryptoKeyPraseException, CryptoDataException, NoSuchProviderException, CryptoAlgParameterException {

        String keyByteString = "D9E5B4485C8388A46707F2D9BD2DE7648A4405605B3683F83F8F1E4CA66231420BEC522688F34B993783ECBDE2352BD5B8E915319B8DBED8CABB3C9A2A6F018794E035746C7C5EBC13284797416F441C9DA39B1E923100B8C63677AC0543CDEC99B7D55E01A63E80F3A37485043C4B4DA731DBD11CEAA1974969D99839C0BD0C00420F046424C6E6FCA3CBDC4764D1C0";
        String keyHash = "23A8E77940B3B73F9FB193B477EE5DA52AA78C4EF55B2EDF160369CD29961AAC";

        Security.addProvider(new SwxaProvider(null, null));
        System.setProperty("SANSEC.SSL", "TRUE");
//        byte[] keyBlob = Base64.decodeBase64("jGL0p/PXATUaTlmm+yrT8/hkeqMi1OVyT8Ab9gFb+Q4=");
        byte[] keyBlob = Base64.decodeBase64("EvYiocZaIZ85LFJzC8J0sg==");
        byte[] plainBlob = "SWXA1234@DAR_Mwqewdsfsddfsqewqeysql".getBytes();
        byte[] res = CryptoUtils.mac(keyBlob, Hex.decodeHex(keyByteString), "HMACSM3");
        String mac = Hex.encodeHexString(res);
        System.out.println(mac);
        System.out.println(keyHash.equalsIgnoreCase(mac));
        System.out.println(res.length);

    }
}
