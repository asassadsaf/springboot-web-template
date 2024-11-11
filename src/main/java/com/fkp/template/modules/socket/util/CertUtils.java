package com.fkp.template.modules.socket.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description 读取证书文件工具类
 * @date 2024/10/25 15:05
 */
public class CertUtils {

//    private static final String PROVIDE_NAME = "SwxaJCE";
    private static final String PROVIDE_NAME = BouncyCastleProvider.PROVIDER_NAME;

    private CertUtils(){}

    public static KeyStore genKeyStoreByPkcs12(String filePath, String pwd) throws KeyStoreException, NoSuchProviderException, CertificateException, IOException, NoSuchAlgorithmException {
        try (InputStream in = Files.newInputStream(Paths.get(filePath))){
            KeyStore keyStore = KeyStore.getInstance("PKCS12", PROVIDE_NAME);
            keyStore.load(in, pwd.toCharArray());
            return keyStore;
        }
    }

    public static KeyStore genKeyStoreByX509Pem(String filePath) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, NoSuchProviderException {
        try (InputStream in = Files.newInputStream(Paths.get(filePath))){
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType(), PROVIDE_NAME);
            keyStore.load(null, null);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", PROVIDE_NAME);
            Certificate certificate = certificateFactory.generateCertificate(in);
            keyStore.setCertificateEntry("root-ca", certificate);
            return keyStore;
        }
    }
}
