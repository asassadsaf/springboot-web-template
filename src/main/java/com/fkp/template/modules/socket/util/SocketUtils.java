package com.fkp.template.modules.socket.util;

import cn.hutool.core.net.DefaultTrustManager;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/10/27 14:04
 */
public class SocketUtils {
    private SocketUtils(){}

    public static SSLSocketFactory  genSslSocketFactory(String keyStorePath, String pwd, String trustKeyStorePath) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, CertificateException, IOException, NoSuchProviderException {
        KeyManager[] keyManagers = genKeyManagerFactory(CertUtils.genKeyStoreByPkcs12(keyStorePath, pwd), pwd);
        TrustManager[] trustManagers = genTrustManagerFactory(CertUtils.genKeyStoreByX509Pem(trustKeyStorePath));
        SSLContext sslContext = genSslContext(keyManagers, genNoValidateTrustManager(), new SecureRandom());
        return sslContext.getSocketFactory();
    }

    public static SSLServerSocketFactory  genSslServerSocketFactory(String keyStorePath, String pwd, String trustKeyStorePath) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, CertificateException, IOException, NoSuchProviderException {
        KeyManager[] keyManagers = genKeyManagerFactory(CertUtils.genKeyStoreByPkcs12(keyStorePath, pwd), pwd);
        TrustManager[] trustManagers = genTrustManagerFactory(CertUtils.genKeyStoreByX509Pem(trustKeyStorePath));
        SSLContext sslContext = genSslContext(keyManagers, trustManagers, new SecureRandom());
        return sslContext.getServerSocketFactory();
    }

    private static SSLContext genSslContext(KeyManager[] keyManagers, TrustManager[] trustManagers, SecureRandom secureRandom) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, trustManagers, secureRandom);
        return sslContext;
    }

    private static KeyManager[] genKeyManagerFactory(KeyStore keyStore, String pwd) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, pwd.toCharArray());
        return keyManagerFactory.getKeyManagers();
    }

    private static TrustManager[] genTrustManagerFactory(KeyStore keyStore) throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(keyStore);
        return trustManagerFactory.getTrustManagers();
    }

    /**
     * 返回一个不认证对方ssl证书的TrustManager数组，即无需传入对方ssl证书的根证书
     * @return 不认证根证书的TrustManager数组
     */
    private static TrustManager[] genNoValidateTrustManager(){
        // 使用hutool工具类中的DefaultTrustManager等同于以下代码
//        TrustManager trustManager = new X509TrustManager() {
//            @Override
//            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}
//            @Override
//            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}
//            @Override
//            public X509Certificate[] getAcceptedIssuers() {
//                return new X509Certificate[0];
//            }
//        };
        return new TrustManager[]{new DefaultTrustManager()};
    }
}
