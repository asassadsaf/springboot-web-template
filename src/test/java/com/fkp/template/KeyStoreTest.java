package com.fkp.template;

import cn.hutool.core.util.EnumUtil;
import com.fkp.template.modules.socket.util.KeyStores;
import com.sansec.jcajce.provider.asymmetric.sm2.JCESM2PrivateKey;
import com.sansec.jce.provider.SwxaProvider;
import com.sansec.tlcp.jsse.provider.SwxaJsseProvider;
import com.sun.crypto.provider.SunJCE;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.EnumerationUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.EnumUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sun.security.provider.X509Factory;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/11/12 14:51
 */
public class KeyStoreTest {

    @BeforeAll
    static void init(){
        Security.addProvider(new SwxaProvider());
        Security.addProvider(new SwxaJsseProvider());
        Security.addProvider(new BouncyCastleProvider());
    }

    @SneakyThrows
    @Test
    void test(){
        KeyStore keyStore = KeyStore.getInstance("JKS", "SwxaJCE");
        keyStore.load(new FileInputStream("D:/IDEAWorkSpace/springboot-web-template/config/certs/SM2/sm2cert.jks"), "swxa1234.".toCharArray());
        System.out.println(keyStore);
        System.out.println(EnumerationUtils.toList(keyStore.aliases()));
        // 证书对象，可以获取公钥
        Certificate certificate = keyStore.getCertificate("sm2cert");
        Certificate certificate2 = keyStore.getCertificate("sm2cert_2");
        Certificate rootca = keyStore.getCertificate("rootca");
//        System.out.println(certificate);
//        System.out.println(certificate2);
//        System.out.println(rootca);
        System.out.println(Base64.encodeBase64String(certificate.getEncoded()));
        System.out.println(Base64.encodeBase64String(certificate2.getEncoded()));
        System.out.println(Base64.encodeBase64String(rootca.getEncoded()));
        // 私钥对象
        Key key = keyStore.getKey("sm2cert", "swxa1234.".toCharArray());
        Key key2 = keyStore.getKey("sm2cert_2", "swxa1234.".toCharArray());
        Key rootcaKey = keyStore.getKey("rootca", "swxa1234.".toCharArray());
        System.out.println(key);
        System.out.println(key2);
        System.out.println(rootcaKey);
    }

    @SneakyThrows
    @Test
    void test1(){
        KeyStore keyStore = KeyStore.getInstance("SWKS", "SwxaJCE");
        keyStore.load(new ByteArrayInputStream(KeyStores.swks_server_sm2), "66666666".toCharArray());
        System.out.println(keyStore);
        System.out.println(EnumerationUtils.toList(keyStore.aliases()));
        // 证书对象，可以获取公钥
        Certificate certificate = keyStore.getCertificate("pfx_1_1");
        Certificate certificate2 = keyStore.getCertificate("pfx_2_1");
        Certificate rootca = keyStore.getCertificate("Root");
//        System.out.println(certificate);
//        System.out.println(certificate2);
//        System.out.println(rootca);
        System.out.println(Base64.encodeBase64String(certificate.getEncoded()));
        System.out.println(Base64.encodeBase64String(certificate2.getEncoded()));
        System.out.println(Base64.encodeBase64String(rootca.getEncoded()));
        // 私钥对象
        Key key = keyStore.getKey("pfx_1_1", "66666666".toCharArray());
        Key key2 = keyStore.getKey("pfx_2_1", "66666666".toCharArray());
        Key rootcaKey = keyStore.getKey("Root", "66666666".toCharArray());
        System.out.println(key);
        System.out.println(key2);
        System.out.println(rootcaKey);
    }

    @SneakyThrows
    @Test
    void test3(){
        KeyStore keyStore = KeyStore.getInstance("PKCS12", "SwxaJCE");
        keyStore.load(new FileInputStream("D:/IDEAWorkSpace/springboot-web-template/config/certs/SM2/sm2cert.pfx"), "swxa1234.".toCharArray());
        System.out.println(keyStore);
        System.out.println(EnumerationUtils.toList(keyStore.aliases()));
        Certificate certificate = keyStore.getCertificate("pfx");
        Certificate[] certificateChain = keyStore.getCertificateChain("pfx");
        System.out.println(Base64.encodeBase64String(certificate.getEncoded()));
        // pfx证书中在证书链中也可以获取到根证书
        for (Certificate certificate1 : certificateChain) {
            System.out.println(Base64.encodeBase64String(certificate1.getEncoded()));
        }
        Key key = keyStore.getKey("pfx", "66666666".toCharArray());
        System.out.println(key);
        // pfx证书也可以保存多个证书
        keyStore.setCertificateEntry("fkp", certificate);;
        System.out.println(EnumerationUtils.toList(keyStore.aliases()));
        //        // 证书对象，可以获取公钥
//        Certificate certificate = keyStore.getCertificate("sm2cert");
//        Certificate certificate2 = keyStore.getCertificate("sm2cert_2");
//        Certificate rootca = keyStore.getCertificate("rootca");
////        System.out.println(certificate);
////        System.out.println(certificate2);
////        System.out.println(rootca);
//        System.out.println(Base64.encodeBase64String(certificate.getEncoded()));
//        System.out.println(Base64.encodeBase64String(certificate2.getEncoded()));
//        System.out.println(Base64.encodeBase64String(rootca.getEncoded()));
//        // 私钥对象
//        Key key = keyStore.getKey("sm2cert", "swxa1234.".toCharArray());
//        Key key2 = keyStore.getKey("sm2cert_2", "swxa1234.".toCharArray());
//        Key rootcaKey = keyStore.getKey("rootca", "swxa1234.".toCharArray());
//        System.out.println(key);
//        System.out.println(key2);
//        System.out.println(rootcaKey);
    }

    @SneakyThrows
    @Test
    void test2(){
        String encCertPem = IOUtils.toString(URI.create("file:///D:/IDEAWorkSpace/springboot-web-template/config/certs/ccsp-x86/sm2_ccsp_enc.pem"), StandardCharsets.UTF_8);
        String encCertStr = encCertPem.replaceAll("-----BEGIN CERTIFICATE-----", "").replaceAll("-----END CERTIFICATE-----", "").replaceAll("\n\r", "");
        String signCertPem = IOUtils.toString(URI.create("file:///D:/IDEAWorkSpace/springboot-web-template/config/certs/ccsp-x86/sm2_ccsp_sign.pem"), StandardCharsets.UTF_8);
        String signCertStr = signCertPem.replaceAll("-----BEGIN CERTIFICATE-----", "").replaceAll("-----END CERTIFICATE-----", "").replaceAll("\n\r", "");
        String rootCertPem = IOUtils.toString(URI.create("file:///D:/IDEAWorkSpace/springboot-web-template/config/certs/ccsp-x86/SANSEC_PD_ROOT(SM2).cer"), StandardCharsets.UTF_8);
        String rootCertStr = rootCertPem.replaceAll("-----BEGIN CERTIFICATE-----", "").replaceAll("-----END CERTIFICATE-----", "").replaceAll("\n\r", "");
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509", "SwxaJCE");
        Certificate encCert = certFactory.generateCertificate(IOUtils.toInputStream(encCertStr, StandardCharsets.UTF_8));
        Certificate signCert = certFactory.generateCertificate(IOUtils.toInputStream(signCertStr, StandardCharsets.UTF_8));
        Certificate rootCert = certFactory.generateCertificate(IOUtils.toInputStream(rootCertStr, StandardCharsets.UTF_8));

        String encPriPem = IOUtils.toString(URI.create("file:///D:/IDEAWorkSpace/springboot-web-template/config/certs/ccsp-x86/sm2_ccsp_enc_pri.pem"), StandardCharsets.UTF_8);
        String signPriPem = IOUtils.toString(URI.create("file:///D:/IDEAWorkSpace/springboot-web-template/config/certs/ccsp-x86/sm2_ccsp_sign_pri.pem"), StandardCharsets.UTF_8);
        String encPriStr = encPriPem.replaceAll("-----BEGIN PRIVATE KEY-----", "").replaceAll("-----END PRIVATE KEY-----", "").replaceAll("\r\n", "");
        String signPriStr = signPriPem.replaceAll("-----BEGIN PRIVATE KEY-----", "").replaceAll("-----END PRIVATE KEY-----", "").replaceAll("\r\n", "");
        PKCS8EncodedKeySpec encPriSpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(encPriStr));
        PKCS8EncodedKeySpec signPriSpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(signPriStr));
        KeyFactory keyFactory = KeyFactory.getInstance("SM2", "SwxaJCE");
        JCESM2PrivateKey encPriKey = (JCESM2PrivateKey) keyFactory.generatePrivate(encPriSpec);
        JCESM2PrivateKey signPriKey = (JCESM2PrivateKey) keyFactory.generatePrivate(signPriSpec);

        KeyStore keyStore = KeyStore.getInstance("JKS", "SwxaJCE");
        keyStore.load(null, null);
        keyStore.setKeyEntry("enc", encPriKey, "swxa1234.".toCharArray(), new Certificate[]{encCert, rootCert});
        keyStore.setKeyEntry("sign", signPriKey, "swxa1234.".toCharArray(), new Certificate[]{signCert, rootCert});
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        keyStore.store(outputStream, "swxa1234.".toCharArray());
        byte[] byteArray = outputStream.toByteArray();
        String base64String = Base64.encodeBase64String(byteArray);
        // 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
        System.out.println(base64String);
        File file = new File("D:/IDEAWorkSpace/springboot-web-template/config/certs/ccsp-x86/sm2_ccsp.jks");
        if(file.exists()){
            file.delete();
        }
        FileUtils.writeByteArrayToFile(file, byteArray);
    }

    @SneakyThrows
    @Test
    void test4(){
        // file://通常表示网络文件系统的路径，file:///表示本地文件路径
        byte[] byteArray = IOUtils.toByteArray(URI.create("file:///D:/IDEAWorkSpace/springboot-web-template/config/certs/ccsp-x86/sm2_ccsp_enc.key"));
        System.out.println(Arrays.toString(byteArray));
        System.out.println(byteArray.length);
        System.out.println(Base64.encodeBase64String(byteArray));
        byte[] byteArray2 = IOUtils.toByteArray(URI.create("file:///D:/IDEAWorkSpace/springboot-web-template/config/certs/ccsp-x86/sm2_ccsp_sign.key"));
        System.out.println(Arrays.toString(byteArray2));
        System.out.println(byteArray2.length);
        System.out.println(Base64.encodeBase64String(byteArray2));
    }

    @SneakyThrows
    @Test
    void test5(){
        String certPem = IOUtils.toString(URI.create("file:///D:/IDEAWorkSpace/springboot-web-template/config/certs/ccsp-x86/sm2_ccsp_enc.pem"), StandardCharsets.UTF_8);
        String certStr = certPem.replaceAll("-----BEGIN CERTIFICATE-----", "").replaceAll("-----END CERTIFICATE-----", "").replaceAll("\n\r", "");
        String rootCertPem = IOUtils.toString(URI.create("file:///D:/IDEAWorkSpace/springboot-web-template/config/certs/ccsp-x86/SANSEC_PD_ROOT(SM2).cer"), StandardCharsets.UTF_8);
        String rootCertStr = rootCertPem.replaceAll("-----BEGIN CERTIFICATE-----", "").replaceAll("-----END CERTIFICATE-----", "").replaceAll("\n\r", "");
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509", "SwxaJCE");
        Certificate cert = certFactory.generateCertificate(IOUtils.toInputStream(certStr, StandardCharsets.UTF_8));
        Certificate rootCert = certFactory.generateCertificate(IOUtils.toInputStream(rootCertStr, StandardCharsets.UTF_8));
        cert.verify(rootCert.getPublicKey());
    }
    
    @SneakyThrows
    @Test
    void testGenRSAJKS(){
        KeyStore keyStore = KeyStore.getInstance("PKCS12", "SunJSSE");
        keyStore.load(FileUtils.openInputStream(new File("D:/IDEAWorkSpace/springboot-web-template/config/certs/RSA/pt_rsa_sign_client.pfx")), "swxa@2024".toCharArray());
        // [pfx]
        System.out.println(EnumerationUtils.toList(keyStore.aliases()));
        Certificate certificate = keyStore.getCertificate("pfx");
        RSAPrivateKey privateKey = (RSAPrivateKey) keyStore.getKey("pfx", "swxa@2024".toCharArray());
        // class sun.security.rsa.RSAPrivateCrtKeyImpl
        System.out.println(privateKey.getClass());
        Certificate[] certificateChain = keyStore.getCertificateChain("pfx");
        // 2
        System.out.println(certificateChain.length);
        Certificate userCert = certificateChain[0];
        Certificate rootCa = certificateChain[1];
        // true
        System.out.println(Arrays.equals(certificate.getEncoded(), userCert.getEncoded()));
        // 封装jks
        KeyStore jks = KeyStore.getInstance("JKS", "SUN");
        // SUN
        System.out.println(jks.getProvider().getName());
        jks.load(null, null);
        jks.setKeyEntry("client-cert", privateKey, "swxa@2024".toCharArray(), certificateChain);
        jks.setCertificateEntry("rootCa", rootCa);
        File file = new File("D:/IDEAWorkSpace/springboot-web-template/config/certs/RSA/pt_rsa_sign_client.jks");
        if(file.exists()){
            file.delete();
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        jks.store(outputStream, "swxa@2024".toCharArray());
        byte[] byteArray = outputStream.toByteArray();
        // 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
        System.out.println(Base64.encodeBase64String(byteArray));
        FileUtils.writeByteArrayToFile(file, byteArray);
    }

    @Test
    @SneakyThrows
    void testGenSm2Pfx(){
        String encCertPem = IOUtils.toString(URI.create("file:///D:/IDEAWorkSpace/springboot-web-template/config/certs/ccsp-x86/sm2_ccsp_enc.pem"), StandardCharsets.UTF_8);
        String encCertStr = encCertPem.replaceAll("-----BEGIN CERTIFICATE-----", "").replaceAll("-----END CERTIFICATE-----", "").replaceAll("\n\r", "");
        String signCertPem = IOUtils.toString(URI.create("file:///D:/IDEAWorkSpace/springboot-web-template/config/certs/ccsp-x86/sm2_ccsp_sign.pem"), StandardCharsets.UTF_8);
        String signCertStr = signCertPem.replaceAll("-----BEGIN CERTIFICATE-----", "").replaceAll("-----END CERTIFICATE-----", "").replaceAll("\n\r", "");
        String rootCertPem = IOUtils.toString(URI.create("file:///D:/IDEAWorkSpace/springboot-web-template/config/certs/ccsp-x86/SANSEC_PD_ROOT(SM2).cer"), StandardCharsets.UTF_8);
        String rootCertStr = rootCertPem.replaceAll("-----BEGIN CERTIFICATE-----", "").replaceAll("-----END CERTIFICATE-----", "").replaceAll("\n\r", "");
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509", "SwxaJCE");
        Certificate encCert = certFactory.generateCertificate(IOUtils.toInputStream(encCertStr, StandardCharsets.UTF_8));
        Certificate signCert = certFactory.generateCertificate(IOUtils.toInputStream(signCertStr, StandardCharsets.UTF_8));
        Certificate rootCert = certFactory.generateCertificate(IOUtils.toInputStream(rootCertStr, StandardCharsets.UTF_8));

        String encPriPem = IOUtils.toString(URI.create("file:///D:/IDEAWorkSpace/springboot-web-template/config/certs/ccsp-x86/sm2_ccsp_enc_pri.pem"), StandardCharsets.UTF_8);
        String signPriPem = IOUtils.toString(URI.create("file:///D:/IDEAWorkSpace/springboot-web-template/config/certs/ccsp-x86/sm2_ccsp_sign_pri.pem"), StandardCharsets.UTF_8);
        String encPriStr = encPriPem.replaceAll("-----BEGIN PRIVATE KEY-----", "").replaceAll("-----END PRIVATE KEY-----", "").replaceAll("\r\n", "");
        String signPriStr = signPriPem.replaceAll("-----BEGIN PRIVATE KEY-----", "").replaceAll("-----END PRIVATE KEY-----", "").replaceAll("\r\n", "");
        PKCS8EncodedKeySpec encPriSpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(encPriStr));
        PKCS8EncodedKeySpec signPriSpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(signPriStr));
        KeyFactory keyFactory = KeyFactory.getInstance("SM2", "SwxaJCE");
        JCESM2PrivateKey encPriKey = (JCESM2PrivateKey) keyFactory.generatePrivate(encPriSpec);
        JCESM2PrivateKey signPriKey = (JCESM2PrivateKey) keyFactory.generatePrivate(signPriSpec);
        KeyStore keyStore = KeyStore.getInstance("PKCS12", "SwxaJCE");
        keyStore.load(null, null);
        keyStore.setKeyEntry("enc-cert", encPriKey, "swxa1234.".toCharArray(), new Certificate[]{encCert, rootCert});
        keyStore.setKeyEntry("sign-cert", signPriKey, "swxa1234.".toCharArray(), new Certificate[]{signCert, rootCert});
        keyStore.setCertificateEntry("root-ca", rootCert);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        keyStore.store(outputStream, "swxa1234.".toCharArray());
        byte[] byteArray = outputStream.toByteArray();
        String base64String = Base64.encodeBase64String(byteArray);
        // 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
        System.out.println(base64String);
        File file = new File("D:/IDEAWorkSpace/springboot-web-template/config/certs/ccsp-x86/sm2_ccsp.pfx");
        if(file.exists()){
            file.delete();
        }
        FileUtils.writeByteArrayToFile(file, byteArray);

    }
    
    @SneakyThrows
    @Test
    void testGenSm2JksByTrustPem(){
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", "SwxaJCE");
        Certificate certificate = certificateFactory.generateCertificate(Files.newInputStream(Paths.get("D:/IDEAWorkSpace/springboot-web-template/config/certs/ccsp-x86/root-ca.cer"), StandardOpenOption.READ));
        KeyStore keyStore = KeyStore.getInstance("JKS", "SwxaJCE");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("root-ca", certificate);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        keyStore.store(outputStream, "swxa1234.".toCharArray());
        byte[] byteArray = outputStream.toByteArray();
        String base64String = Base64.encodeBase64String(byteArray);
        // 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
        System.out.println(base64String);
        File file = new File("D:/IDEAWorkSpace/springboot-web-template/config/certs/ccsp-x86/root-ca.jks");
        if(file.exists()){
            file.delete();
        }
        FileUtils.writeByteArrayToFile(file, byteArray);

    }

    @SneakyThrows
    @Test
    void testGenRSAJksByTrustPem(){
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", "SwxaJCE");
        Certificate certificate = certificateFactory.generateCertificate(Files.newInputStream(Paths.get("D:/IDEAWorkSpace/springboot-web-template/config/certs/RSA/pt_rsa_cer.cer"), StandardOpenOption.READ));
        KeyStore keyStore = KeyStore.getInstance("JKS", "SwxaJCE");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("root-ca", certificate);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        keyStore.store(outputStream, "swxa@2024".toCharArray());
        byte[] byteArray = outputStream.toByteArray();
        String base64String = Base64.encodeBase64String(byteArray);
        // MIACAQMwgAYJKoZIhvcNAQcBoIAkgASCA+gwgDCABgkqhkiG9w0BBwGggCSABIICTjCCAkowggEiBgsqhkiG9w0BDAoBAqCByDCBxTAoBgoqhkiG9w0BDAEDMBoEFF9BOiB+sfkDY3XdT9jVEw/XHDq5AgIEAASBmNl2NuZ+fx6Jr2MqWpmoXiKGt5q6nF8JB4wlm65qHn1xSl9Rsati3torn4N67Xw9XaJAv1arBOJrvqcV+dh69TpP4teGw1Cea0IZP+efYJFlZSVPCwodTRngRgdNtWzBE9zCrZyDyRUzcu2LkSUTaxHPlCFZ2IOqHR3w5u7MS2yEXEWpUrs0pQ0wCUBF0ULdfq10o3zi0X0vMUgwIQYJKoZIhvcNAQkUMRQeEgBzAGkAZwBuAC0AYwBlAHIAdDAjBgkqhkiG9w0BCRUxFgQUCXaCNFBIQGe4kiMNsJhUdQab2TMwggEgBgsqhkiG9w0BDAoBAqCByDCBxTAoBgoqhkiG9w0BDAEDMBoEFLDZI+J1otT6gug9BrfGzPA6TpwuAgIEAASBmPqFdSpw5q2/sIeeLsuwDBU1V0zKg3NThGHz1JK8FIqm4teR2dfCPZ07ad8PtK+Kf8Wwu/mykjwChyr02EoXKYTEs+0pMozc5FOwkSiB1TqxUH4ToNrfr7bd+/4ZRCqESTjUy62YCqj1RQJMXbkq13ptHi308D/Mg6cEce+lsrxYQlpZrn5Hbll+8Kia32MvJpFYdm0oILybMUYwHwYJKoZIhvcNAQkUMRIeEABlAG4AYwAtAGMAZQByAHQwIwYJKoZIhvcNAQkVMRYEFAl2gjRQSEBnuJIjDbCYVHUGm9kzAAAAAAAAMIAGCSqGSIb3DQEHBqCAMIACAQAwgAYJKoZIhvcNAQcBMCgGCiqGSIb3DQEMAQYwGgQUzVmBkaCtoe/J+zGCQB7y1p+W8TQCAgQAoIAEggPo6Jb+tLr5dJpxrR2N8Vcm2KsJhTeVVtd7aL/lybNwU4w0GsNiTJZ96AeaP/Y/ozvk9MRmK8DMyYrXUCuPf/k7XR/Xv8Bkvb72i7d/nPQf3KheKYb3k9ddYXzQHo3xyV8L0J08U+YqZe/7C/UqaRobL2QlR90FvDguSS6JhClRD23CFPjOoZnlRDhfuL9Ds2o40xGxHufObtbo/BMh+/YUlQlGkqdHBVPTdwyPu3czAdGo+Z0pmyGIcm+ElKtrNw2O0SN54RRP75/MXXcXUBrL5Ly+2G1wAuE9jACn8nck5FoiXfLiEUJSW6RCvUCvq53ZNid3dJTcRQYsNeMq/Gkxw/9q2pl+kCUJNZGLnVWk6Eca0VcZRoLlgy3WALWGSSbYH8TJq7HWl+LekuuLBIID6B4Jxq9Lwm4esz6uA6+YsZVSblfKJzU5JxQytNcG+PjRGbRYo362U75WhoaI6r0NjHDhGhIAEE6ngEgisKt32KlvevF6OIlZR1GB7y9/q6kHLY2MG1LbJU8F9Kj0X1fW2nK4qgiu88pa2u7k+2GhVoq70P4+5L3yuEdcWMu8PkC+VvzfwLjK0ftj27TJRGWLuS36giZ8/2WzinQjv5ZnjwSLfoWxyJjn5G942E/qfnetr9LBUaVH5GVe4I8s/F5+Dq7XB9UkkQNDSjl0G6tCTKBw7yxB/JUxjE7nWXNjsXWWg7EafN68dfEHe0vJ99H612f9SHD08YsQNTYLGHPl+hjOVXyNpGl2EcpzbRt+JQyN9sf+BFHrSGiLjvXezzXJ6c467RJHnF+sRZh/eaiJxub9ptU9LF/Zw9Tev16EzTEfnRUWiee4W6lZEnap1mk97ah0/kxRJtQRfLimsAc/Dt1iEgNe1p0qoTtKzvWzJaeMO/W8M6anAyH0ieNC2Sq5TzkA79ALDc1As+Rj0m7R2BpQH/9I8WlAwfA2P5ZS+ncJNUFOhOMD+btYmuHO9YkHppvuS+vygok6YI/m95invGS0U5m8lpqf+E3yy83nwqo8/HNAcu1vUB73bNA2UPgMvxHrGSkK/qnZ4EXduy8Lhh7i5FK32nn6W/bx5kHJHk4ZaaJ1Oh2uJYxrgbYhRjduc2cgtkp+1PiBWkFIhuXGmjjPNfyc/G5fskbBf9GkbQ2MoNocS6yB0DP8VuyZbxacjI0zq0Mxlr0yu8VCCc9IcWI4jGZoKWF1BtRIakO621i0VaCVAXxP4xK9e/M94hSkPSQxdy50rWFFHq3tQrlQ4iwEqlNzNgWqjS/tkk8CWJk7LibfS7icFZ4QxLMXZ9nTkwMea1Fx9NZty83EOVHDvo849L1O9m+vguWHSn8EggPo2Ji64t0ZmCYUlc9XN0oJghSurVk5FAmz7KLd65rpXZ5XskXInua3WRiYuVo7s9cse7+TowbdnW7QKHNoVQTODNRdhfnmMyA2BtSQTQDp94KrgY8e1TkgktfufQNzyjPw36m40wSGExMnPx6SAk29wZKsIpa7rRC9qG8yV7VoXpccV1rG/jNTmTMB+0QNdZcEpRvPRtGpWhNtTx0qPlOKpA+pR1MnHhlLT8N4+6PRZ+frZ/ZRAHzD6+EyemJ0ly/SWQtuq+MN746f7SNvZKfjRhZdU+mAvY/qhk/1tDy6bUpBvOjO3aAKwprpY5Okm3AAWVwCum9vwAHshwvDFe5Hs8V/rBFFfM4/MFLJ/Nd8ReP8DIY8j0IRc9iE9YxEd6L0w3Ot6KsE/QIEggO35B+K0hchIurNtusO6VvDzU3HdAQ1ISfEJSgNfu4lLPgxXJhHGTNlUYrQTHGH+sLdsGRhN52UXCvgHYOIz+UouGl3IPnHJsQxGkIXtMvbdjnrsnm/kXnVzEvKQBnz8h+XhJO4fttb0f336LW0cx83wBXn6JjoexbkIvcE8pxRfd525BA6EvSxGoljLZdv97vDW+v98N3m32wZxruK4evdztt4ZCiP1MbfWHPuDgMuuzqO/Lk95ivD7gZS6BG8PH+dgswKk/Ms2q+Sjp47wetbK7NH0hd56fQGSeYK2Wt1TNDx6K8Lc+9I3T+twf5AezRRbmpP5yBmDn5kXjbmhy4Hbi/bcJSAeYVHWhHGYifuhN5d7or1zdL3+aOVSEMS+MUF1YCzHgAkme74MSIl3P0Zu+RvQqdpJIdxHwTaN+3QlUUpIZxO2iKTEmqc8COolfVACA0wpmX5CZTKaR0WZYfsJhpIEy6UoQBNEY5f4uwbsi/EKK6uYOZfmO1k0a/+NdV3XStKlLkZSiLXKHKW1sGud/ubwUr7qwCDCfqmZrBCvmrGnHwBKwBCHvFrbB2uossRLTr6V6hyXZkTW0Nli1ZpA9KGv9csCEqQIqAL8BPBOYsdDq1U8wur2Gao9aSRNKUGX23qx1csW73VQKksK0MF6vsGjdiLFRGTC7GkITC8hfuXflRmKF9ZkvtcDExCvnceWvRE8QA95rh6FeplrF6zmnh2JdfAnTDvcSkyNrO+HDjCGU+Jo1A3FLL+8QWcpcdkzFqbLwrprblaN4rFWWH4Mfv/B+/sbFqthbMWyujnKXhswI6JQ28bT4wk3vxerePBVelYlFqRJongeipf9SgFU2Rq7IRLgJ2yRD+o4NVyL449S0jzaSHp7DpsZF410kZQD82yodiEonOzCl740r4QpPGngoBd+JWgTO7I1T+7NdIEgeiPiYEW8OOV1MaYUvjUW8WLTZf0h9kHVmimMTUJaKKTOUO3I5sG0m/q7rsgKCF7wVbn3VdP+fmJfkDLV12MdXieGm0wkR4Y5kbDQr9zDbCUqEnQ0M7hkBqftt63AyaPGaWDF7XIkJwaRHWgDoMa8SxUGjs0cyrN0WMjDMDpYlh4H4NuSa+LwRLDGXCU0igkyvJSTwmorotErLKxrDKH/zdN8cfO0lFTxdabzbb91yHU4MSTC9wtTtA4lnrnLTMzh/NVUUCUMtufHyaj7QKsCVkcf7I/TeatNa3e1aj7P2VaZj7sm52l0O15AAAAAAAAAAAAAAAAAAAAAAAAMD0wITAJBgUrDgMCGgUABBSdYsCW/drfr1VcxskQceTPog5DVwQUWiyd2yV7bomvJSsqV78jZ/d0I7wCAgQAAAA=
        System.out.println(base64String);
        File file = new File("D:/IDEAWorkSpace/springboot-web-template/config/certs/RSA/root-ca.jks");
        if(file.exists()){
            file.delete();
        }
        FileUtils.writeByteArrayToFile(file, byteArray);

    }

    @SneakyThrows
    @Test
    void testCertificateFactory(){
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        System.out.println(certificateFactory.getProvider().getName());
    }

}
