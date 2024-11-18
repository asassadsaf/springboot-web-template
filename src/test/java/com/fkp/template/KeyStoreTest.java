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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
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
        keyStore.load(new FileInputStream("D:/IDEAWorkSpace/springboot-web-template/config/certs/SM2/sm2_server.pfx"), "66666666".toCharArray());
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
        keyStore.setCertificateEntry("fkp", certificate);
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

}
