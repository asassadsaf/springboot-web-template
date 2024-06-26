package com.fkp.template;

import com.fkp.template.modules.xkip.dto.request.GenRandomRequest;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;
import org.springframework.util.Base64Utils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/23 19:54
 */
public class JavaTest {

    //不在循环中，创建多个匿名类，其Class名不同class com.fkp.template.JavaTest$1/class com.fkp.template.JavaTest$2...
    @Test
    void testInnerClass() {
        Class<? extends Object> aClass = new Object() {
            private String str;
        }.getClass();
        Class<? extends Object> aClass2 = new Object() {
            private String str;
        }.getClass();
        Class<? extends Object> aClass3 = new Object() {
            private String str;
        }.getClass();
        System.out.println(aClass);
        System.out.println(aClass2);
        System.out.println(aClass3);
    }

    //在循环中，创建多个匿名类，其Class名相同
    @Test
    void testInnerClassForLoop(){
        List<Class<? extends Object>> list = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            Class<? extends Object> aClass = new Object() {
                private int index = finalI;
            }.getClass();
            list.add(aClass);
        }
        list.forEach(System.out::println);
    }

    @Test
    void test(){
        GenRandomRequest build = GenRandomRequest.builder().build();
        System.out.println(build);
    }

    @Test
    void test6(){
        System.out.println(10_000);
    }

    @Test
    void test2() throws IOException, NoSuchAlgorithmException, NoSuchProviderException, CertificateException {
        Security.addProvider(new BouncyCastleProvider());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\fengkunpeng\\Desktop\\cert.cer");
        byte[] bytes = new byte[1024];
        int len;
        while ((len = fileInputStream.read(bytes)) != -1){
            bos.write(bytes, 0, len);
        }
        String s = bos.toString(StandardCharsets.UTF_8.name());
        String s1 = s.replaceAll("-----BEGIN CERTIFICATE-----", "")
                .replace("-----END CERTIFICATE-----", "")
                .replaceAll("\\s", "");
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64Utils.decodeFromString(s1));
        byte[] encoded = x509EncodedKeySpec.getEncoded();

        System.out.println(Base64Utils.encodeToString(encoded));

        MessageDigest digest = MessageDigest.getInstance("SHA256");
        byte[] result = digest.digest(encoded);
        System.out.println(Hex.encodeHexString(result));

        MessageDigest digest2 = MessageDigest.getInstance("SM3");
        byte[] result2 = digest2.digest(encoded);
        System.out.println(Hex.encodeHexString(result2));

        CertificateFactory instance = CertificateFactory.getInstance("X.509");
        Certificate certificate = instance.generateCertificate(new ByteArrayInputStream(Base64Utils.decodeFromString(s1)));
        PublicKey publicKey = certificate.getPublicKey();
        byte[] encoded1 = publicKey.getEncoded();
        byte[] result3 = digest.digest(encoded1);
        System.out.println(Hex.encodeHexString(result3));

        byte[] result4 = digest2.digest(encoded1);
        System.out.println(Hex.encodeHexString(result4));

//        openssl x509 -in cert.pem -outform der | openssl dgst -sha256 5165ef8cd5babac0957c7ac4ffa0b4d15de73f7a1d892b73e416e4e153633b3c
//        openssl x509 -in cert.pem -noout -pubkey | openssl base64 -d | openssl dgst -sm3

    }

    @SneakyThrows
    @Test
    void test3() {
        String[] certAlias = {"server_enc", "server_sign", "root"};
        Security.addProvider(new BouncyCastleProvider());
        KeyStore jks = KeyStore.getInstance("JKS");
//        InputStream resourceAsStream = JavaTest.class.getClassLoader().getResourceAsStream("rsa_server.jks");
        FileInputStream fileInputStream = new FileInputStream("D:\\IDEAWorkSpace\\src\\main\\resources\\rsa_server.jks");
        jks.load(fileInputStream, "swxa@2024".toCharArray());
        for (String alias : certAlias) {
            Certificate cert = jks.getCertificate(alias);
            byte[] certBytes = cert.getEncoded();
            byte[] pubKeyBytes = cert.getPublicKey().getEncoded();
            MessageDigest digestSha256 = MessageDigest.getInstance("SHA256");
            byte[] certSha256 = digestSha256.digest(certBytes);
            System.out.println(Hex.encodeHexString(certSha256));

            byte[] pubKeySha256 = digestSha256.digest(pubKeyBytes);
            System.out.println(Hex.encodeHexString(pubKeySha256));

            MessageDigest digestSm3 = MessageDigest.getInstance("SM3");
            byte[] certSm3 = digestSm3.digest(certBytes);
            System.out.println(Hex.encodeHexString(certSm3));

            byte[] pubKeySm3 = digestSm3.digest(pubKeyBytes);
            System.out.println(Hex.encodeHexString(pubKeySm3));
        }

    }

    @SneakyThrows
    @Test
    void testYml(){
        FileReader reader = new FileReader("C:\\Users\\fengkunpeng\\Desktop\\policy.yml");
        Yaml yaml = new Yaml();
        Map<String, Object> load = yaml.load(reader);
        reader.close();
        System.out.println(load);
        Map<String, Object> map = new LinkedHashMap<>();

        load.put("disableOutputFormPlain", false);
//        load.putAll(Collections.singletonMap("disableOutputFormPlain", true));
        FileWriter writer = new FileWriter("C:\\Users\\fengkunpeng\\Desktop\\policy.yml");
        yaml.dump(load, writer);
        writer.close();
    }

    @SneakyThrows
    @Test
    void test5(){
//        byte[] iv16 = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
//        byte[] iv12 = {0,1,2,3,4,5,6,7,8,9,10,11};
//        System.out.println(Base64Utils.encodeToString(iv16));
//        System.out.println(Base64Utils.encodeToString(iv12));
        String SM4KeyStr = "3b11b7702a0b8a356c32598a";
        byte[] bytes = Hex.decodeHex(SM4KeyStr);
        System.out.println(bytes.length);
//        System.out.println(new String(bytes));
        System.out.println(Base64Utils.encodeToString(bytes));
        String cipherText = "xv7xbtmK11db5TL0nQrpVvN12OBNERKkZBuxHA==";
        System.out.println(Hex.encodeHexString(Base64Utils.decodeFromString(cipherText)));
    }
}
