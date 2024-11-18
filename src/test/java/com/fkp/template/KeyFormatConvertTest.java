package com.fkp.template;

import cn.hutool.core.util.ByteUtil;
import com.kms.util.crypto.CryptoUtils;
import com.sansec.adapter.AdapterUtils;
import com.sansec.jcajce.provider.asymmetric.sm2.JCESM2PrivateKey;
import com.sansec.jcajce.provider.asymmetric.sslsm2.JCESM2PrivateKey4Openssl;
import com.sansec.jce.provider.SwxaProvider;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/8/4 15:30
 */
public class KeyFormatConvertTest {

    @BeforeAll
    static void addProvider(){
        Security.addProvider(new SwxaProvider());
    }

    @SneakyThrows
    @Test
    void sm2Convert(){
        String asn1PriKey = "MIGIAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBG4wbAIBAQIhAIj6PCRle3Cz+b1Nv5z4vHtkIXBuPTD8be3aEpGiR5QooUQDQgAEyZDlRJ56uhcPsR0pGioop5W+xv72QEncV45/jt6T6/5dJDbfoeDUT8XDdXNUrOAx8UXF/XIAKjG4YKYi9HO5kQ==";
        String asn1PubKey = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEyZDlRJ56uhcPsR0pGioop5W+xv72QEncV45/jt6T6/5dJDbfoeDUT8XDdXNUrOAx8UXF/XIAKjG4YKYi9HO5kQ==";
        byte[] gmPriKey = CryptoUtils.convertSM2PrivateToGM(Base64.decodeBase64(asn1PriKey));
        System.out.println(Base64.encodeBase64String(gmPriKey));
        System.out.println(Arrays.toString(gmPriKey));
        System.out.println(ByteUtil.bytesToInt(Arrays.copyOfRange(gmPriKey, 0, 4)));
        System.out.println(gmPriKey.length);
        byte[] gmPubKey = CryptoUtils.convertSM2PublicToGM(Base64.decodeBase64(asn1PubKey));
        System.out.println(Base64.encodeBase64String(gmPubKey));
        System.out.println(Hex.encodeHexString(gmPubKey));
        System.out.println(ByteUtil.bytesToInt(Arrays.copyOfRange(gmPubKey, 0, 4)));
        System.out.println(gmPubKey.length);
        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPubKey, 4, 4 + 64)));
        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPubKey, 4 + 64, 4 + 64 + 64)));

        byte[] convertAsn1PriKeyBytes = CryptoUtils.convertSM2PrivateToAsn1(gmPriKey, gmPubKey);
        String convertAsn1PriKey = Base64.encodeBase64String(convertAsn1PriKeyBytes);
        System.out.println(convertAsn1PriKey);
        byte[] convertAsn1PubKeyBytes = CryptoUtils.convertSM2PublicToAsn1(gmPubKey);
        String convertAsn1PubKey = Base64.encodeBase64String(convertAsn1PubKeyBytes);
        System.out.println(convertAsn1PubKey);
        System.out.println(convertAsn1PriKey.equals(asn1PriKey));
        System.out.println(convertAsn1PubKey.equals(asn1PubKey));

    }

    @SneakyThrows
    @Test
    void rsaConvert(){
        String asn1PriKey = "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";
        String asn1PubKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAnJ+8cQhGstYiZeDyX/fe+1apoQ4DN2y99ZnI3chjM74Fcos0Yd8A4T8x18P2iTEg5XEsGkEw1gy+FeigUDPPK0tYVqB3SYxbgVjA93dAjfJTlrffusQd/brc0/dlgJf35mM5Na5q5Iej05InaCrc2edyKetv9QrTXMDr9/XI2jtJMBFHWJBo2pJ61py2T6GQkSCkFznRfcRhK9F+CGPLt+fhvk2bOUQWZotLJKxmxV4PIgwwhJZut7jOB9li6NSCPQoVwZgOxuvZEW2mDh83rxYpbzxI3NGALcRKUITKT2YygqIj+Mqdxw92zEso2gwr/qDP9kRFHoGBiAk3GTZ3JVXlz/46cOMPlX5paeC9v426DQ5hrPINzh9qZ35KPlgOpqlQ3j2FOw+7rsh2Hx4Hs8Yll3aBiLSDcy0+l6v4NcdNb0+COHIoEXA2B1jMjtfv0+PK9snKxR4YYkgtE5shbPNerr9a+qutlDeKs1b+tecn5EEkLgaX/I7/Hbu8iWTzz3ImExO3ZR1bpIdIYTMu88NfKaoJiqhtUT3h5cMjSj1Sz9M5gZ/gecq7Y2Ny+ZQmTvwKp4ruR8ySHlA0XSdwuHVl8IXhv2joQ/4PU72ZykyVWSXVN0mQdEihzNLgindxCKsC9eMIn+BiOriY2UBlycjYsVZG1War3VImRMINM7ECAwEAAQ==";
        byte[] gmPriKey = AdapterUtils.adapterPriKey(Base64.decodeBase64(asn1PriKey), "RSA_GM");
        System.out.println(Base64.encodeBase64String(gmPriKey));
//        System.out.println(Arrays.toString(gmPriKey));
        // 模长 1024
        System.out.println(ByteUtil.bytesToInt(Arrays.copyOfRange(gmPriKey, 0, 4)));
        // 1412
        System.out.println(gmPriKey.length);
        //bits 4
        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPriKey, 0, 4)));
        //M 256
        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPriKey, 4, 4 + 256)));
        //E 256
        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPriKey, 4 + 256, 4 + 256 + 256)));
        //D 256
        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPriKey, 4 + 256 + 256, 4 + 256 + 256 + 256)));
        //p 128
        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPriKey, 4 + 256 + 256 + 256, 4 + 256 + 256 + 256 + 128)));
        //q 128
        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPriKey, 4 + 256 + 256 + 256 + 128, 4 + 256 + 256 + 256 + 128 + 128)));
        //Dp 128
        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPriKey, 4 + 256 + 256 + 256 + 128 + 128, 4 + 256 + 256 + 256 + 128 + 128 + 128)));
        //Dq 128
        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPriKey, 4 + 256 + 256 + 256 + 128 + 128 + 128, 4 + 256 + 256 + 256 + 128 + 128 + 128 + 128)));
        //i 128
        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPriKey, 4 + 256 + 256 + 256 + 128 + 128 + 128 + 128, 4 + 256 + 256 + 256 + 128 + 128 + 128 + 128 + 128)));
        // 1412 == gmPriKey.length
        System.out.println(4 + 256 + 256 + 256 + 128 + 128 + 128 + 128 + 128);
        byte[] gmPubKey = AdapterUtils.adapterPubKey(Base64.decodeBase64(asn1PubKey), "RSA_GM");
        System.out.println(Base64.encodeBase64String(gmPubKey));
        //模长 1024
        System.out.println(ByteUtil.bytesToInt(Arrays.copyOfRange(gmPubKey, 0, 4)));
        // 516
        System.out.println(gmPubKey.length);
        //bits 4
        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPubKey, 0, 4)));
        // M 256
        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPubKey, 4, 4 + 256)));
        // E 256
        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPubKey, 4 + 256, 4 + 256 + 256)));
        // 516 == gmPubKey.length
        System.out.println(4 + 256 + 256);

        byte[] convertAsn1PriKeyBytes = CryptoUtils.convertRSAPrivateToAsn1(gmPriKey);
        String convertAsn1PriKey = Base64.encodeBase64String(convertAsn1PriKeyBytes);
        System.out.println(convertAsn1PriKey);
        byte[] convertAsn1PubKeyBytes = CryptoUtils.convertRSAPublicToAsn1(gmPubKey);
        String convertAsn1PubKey = Base64.encodeBase64String(convertAsn1PubKeyBytes);
        System.out.println(convertAsn1PubKey);
        System.out.println(convertAsn1PriKey.equals(asn1PriKey));
        System.out.println(convertAsn1PubKey.equals(asn1PubKey));
    }

    @SneakyThrows
    @Test
    void test(){
        long bytes1 = 0xffffffffL;
        System.out.println(bytes1);
//        byte[] bytes = ByteUtil(10240);
//        System.out.println(Arrays.toString(bytes));
    }

    @SneakyThrows
    @Test
    void testConvertCipher(){
        String cipher = "MHoCIQC2rmguuxW7f+cRYyYA2a3V5yLYZ8v+/aq/oMNAK9FqMwIhAI0tEWgxkdkO868eQ/3TyRuoHfueasAqz1EZ2ZrLZ7swBCDxL8bAMpNoxzrUOJZNwyEQffjhNz1e4qkXq7psrpuyzAQQBPQCiGCRwl/lHjsugp2f4g==";
        byte[] bytes = CryptoUtils.convertSM2CipherTo0018(Base64.decodeBase64(cipher));
        String gmCipher = Base64.encodeBase64String(bytes);
        System.out.println(gmCipher);

    }

    @SneakyThrows
    @Test
    void testConvert(){
        String hex = "308201AC30820151A00302010202082767970D2D9314E6300C06082A811CCF55018375050030413110300E06035504030C07534D32524F4F54310F300D060355040A0C0653414E534543310F300D060355040B0C0653414E534543310B300906035504061302434E3020170D3230303531313132323630315A180F32313230303431373132323630315A30413110300E06035504030C07534D32524F4F54310F300D060355040A0C0653414E534543310F300D060355040B0C0653414E534543310B300906035504061302434E3059301306072A8648CE3D020106082A811CCF5501822D03420004812526F14AD6D4F0ABB0546908F45958D95C9B9459FF051692ADF9374D8286328EA9A838E56D9A5BD4BA1214E121F68CF9629D544A93EF4949168D96CE59E9AAA32F302D301D0603551D0E041604142BC03C2E6A3D26AA199EC9143493201B1BAED0E9300C0603551D13040530030101FF300C06082A811CCF5501837505000347003044022054740C7711B8CA7E18B54A625A7E99B7D19C5689DEB2C3698D829A074CF066D4022073E0066F4276EB748EC72201B1A9A3EDBB5417E941925BFE8149C0FB1BD5CA00";
        System.out.println(Base64.encodeBase64String(Hex.decodeHex(hex)));
    }

    @Test
    void testBytes(){
        byte[] bytes = RandomUtils.nextBytes(3072);
        System.out.println(bytes.length);
        System.out.println(Base64.encodeBase64String(bytes));
    }

    @Test
    void test1(){
        String base64Str = "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";
        System.out.println(Base64.decodeBase64(base64Str).length);
    }

    @SneakyThrows
    @Test
    void test2(){
//        String hexSm2GmPubKey = "0001000000000000000000000000000000000000000000000000000000000000000000002CEE9067ED4519D09E5DBC6E4FBB2B909B429B01B420875F37B4098870FC8CB700000000000000000000000000000000000000000000000000000000000000009828CF157B429F433A8D676A506F3D202542B1AC50D6D1D792E1E9AB81CF7C2A00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
//        byte[] pubKeyGm = Hex.decodeHex(hexSm2GmPubKey);
//        System.out.println(Arrays.toString(pubKeyGm));
//        System.out.println(Base64.encodeBase64String(pubKeyGm));
//        System.out.println(ByteUtil.bytesToInt(Arrays.copyOfRange(pubKeyGm, 0, 4)));
//        System.out.println(pubKeyGm.length);
//        System.out.println(hexSm2GmPubKey.length());
//        byte[] bytes = CryptoUtils.convertSM2PublicToAsn1(Hex.decodeHex(hexSm2GmPubKey));
//        byte[] bytes = CryptoUtils.convertSM2PublicToAsn1(Base64.decodeBase64("AAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAALO6QZ+1FGdCeXbxuT7srkJtCmwG0IIdfN7QJiHD8jLcAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJgozxV7Qp9DOo1nalBvPSAlQrGsUNbR15Lh6auBz3wq"));
//        byte[] bytes = CryptoUtils.convertSM2PublicToAsn1(Base64.decodeBase64("AAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAALO6QZ+1FGdCeXbxuT7srkJtCmwG0IIdfN7QJiHD8jLcAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJgozxV7Qp9DOo1nalBvPSAlQrGsUNbR15Lh6auBz3wqAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=="));
//        System.out.println(Base64.encodeBase64String(bytes));

        byte[] key1 = Base64.decodeBase64("AAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAALO6QZ+1FGdCeXbxuT7srkJtCmwG0IIdfN7QJiHD8jLcAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJgozxV7Qp9DOo1nalBvPSAlQrGsUNbR15Lh6auBz3wq");
        byte[] key2 = Base64.decodeBase64("AAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAALO6QZ+1FGdCeXbxuT7srkJtCmwG0IIdfN7QJiHD8jLcAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJgozxV7Qp9DOo1nalBvPSAlQrGsUNbR15Lh6auBz3wqAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA==");
        System.out.println(key1.length);
        System.out.println(key2.length);
    }

    @Test
    void test3() throws KeyStoreException, NoSuchProviderException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException {
        String keyStoreStr = "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";
        byte[] keyStoreBytes = Base64.decodeBase64(keyStoreStr);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(keyStoreBytes);
        KeyStore keyStore = KeyStore.getInstance("JKS", "SwxaJCE");
        keyStore.load(byteArrayInputStream, "66666666".toCharArray());
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()){
            System.out.println(aliases.nextElement());
        }
        PrivateKey server = (PrivateKey) keyStore.getKey("server", "66666666".toCharArray());
        byte[] encoded = server.getEncoded();
        System.out.println(Base64.encodeBase64String(encoded));
        Certificate certificate = keyStore.getCertificate("server");
        if (certificate instanceof X509Certificate) {
            X509Certificate x509Certificate = (X509Certificate) certificate;
            byte[] pubKey = x509Certificate.getPublicKey().getEncoded();
            System.out.println(Base64.encodeBase64String(pubKey));
            byte[] cert = x509Certificate.getEncoded();
            System.out.println(Base64.encodeBase64String(cert));
        }
//        System.out.println(server);
    }


    /**
     * 对0009PKCS8格式SM2明文密钥材料转为OpenSSL格式
     *
     * @param keyBytes
     * @return status msg keyInfo
     */
    @SneakyThrows
    public static byte[] convert2OCTET(byte[] keyBytes) {

        //使用DER编码的数组，重新构造SM2私钥对象
        PKCS8EncodedKeySpec priSpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory fc = KeyFactory.getInstance("SM2", "SwxaJCE");
        JCESM2PrivateKey newPrivate = (JCESM2PrivateKey) fc.generatePrivate(priSpec);

        //转换为Openssl格式的SM2对象
        JCESM2PrivateKey4Openssl openSslPriKey = new JCESM2PrivateKey4Openssl(newPrivate);
        return openSslPriKey.getEncoded();
    }

    @SneakyThrows
    @Test
    void testOctet2Gm0009(){
        String content = IOUtils.toString(URI.create("file:///D:/IDEAWorkSpace/springboot-web-template/config/certs/ccsp-x86/sm2_ccsp_enc_pri.pem"), StandardCharsets.UTF_8);
        String pem = content.replaceAll("-----BEGIN PRIVATE KEY-----", "").replaceAll("-----END PRIVATE KEY-----", "").replaceAll("\r\n", "");
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.decodeBase64(pem));
        KeyFactory keyFactory = KeyFactory.getInstance("SM2", "SwxaJCE");
        PrivateKey privateKey = keyFactory.generatePrivate(spec);
        System.out.println(privateKey.getClass());
        JCESM2PrivateKey sm2PriKey = (JCESM2PrivateKey) privateKey;
        // 通过构造私钥对象直接将OCTET格式变为0009格式
        byte[] encoded = sm2PriKey.getEncoded();
        System.out.println(Base64.encodeBase64String(encoded));
    }

}
