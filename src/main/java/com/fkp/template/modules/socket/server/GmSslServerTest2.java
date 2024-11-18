package com.fkp.template.modules.socket.server;

import com.fkp.template.modules.socket.util.KeyStores;
import com.sansec.jce.provider.SwxaProvider;
import com.sansec.tlcp.jsse.provider.SwxaJsseProvider;
import io.netty.util.internal.StringUtil;
import org.apache.commons.io.IOUtils;

import javax.net.ssl.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.Security;

public class GmSslServerTest2 {
    private static String ProtocalVersion = "TLCPv1.1";
    private static final String PROVIDER = "SwxaJSSE";
    private static final String ALGRITHM = "SwxaX509";      //SwxaX509 X509
    private static String KS_TYPE = "JKS";                  //SWKS JKS

    /*客户端Key Store*/
   /* private byte[] serverKeyStoreDate = KeyStores.swks_server_sm2;
    private byte[] serverTrustStoreDate = KeyStores.swks_server_sm2_trustStore;*/

    static {
        Security.addProvider(new SwxaProvider(null,null));
    }
    private byte[] serverKeyStoreDate ;
    private byte[] serverTrustStoreDate ;

    char[] keyFilePass;
    char[] keyPass;// 密钥别名的密码

    public GmSslServerTest2(byte[] serverKeyStoreDate, byte[] serverTrustStoreDate, char[] keyFilePass, char[] keyPass) {
        this.serverKeyStoreDate = serverKeyStoreDate;
        this.serverTrustStoreDate = serverTrustStoreDate;
        this.keyFilePass = keyFilePass;
        this.keyPass = keyPass;
    }

    public void startSSLServer(boolean clientAuth, int port_no,String cipherSuit) throws Exception {
        /* JCE Provider信息*/
        SwxaProvider swxaJce = new SwxaProvider(null, null);
        System.setProperty("SANSEC.SSL", "TRUE");
        //添加jsse provider
        Security.addProvider(new SwxaJsseProvider());


        /*服务端KeyStore*/
        KeyStore serverKeyStore = KeyStore.getInstance(KS_TYPE, swxaJce);
        serverKeyStore.load(new ByteArrayInputStream(serverKeyStoreDate), keyFilePass);

        /*服务端TrustKeyStore*/
        KeyStore trustKeyStore_server = KeyStore.getInstance(KS_TYPE, swxaJce);
        trustKeyStore_server.load(new ByteArrayInputStream(serverTrustStoreDate), keyFilePass);


        /*服务端KeyManagerFactory*/
        KeyManagerFactory keyMgrFact = KeyManagerFactory.getInstance(ALGRITHM, PROVIDER);
        keyMgrFact.init(serverKeyStore, keyPass);

        /*服务端TrustManagerFactory*/
        TrustManagerFactory trustMgrFact = TrustManagerFactory.getInstance("PKIX", PROVIDER);
        trustMgrFact.init(trustKeyStore_server);


        /*SSL上下文*/
        SSLContext sslContext = SSLContext.getInstance(ProtocalVersion, PROVIDER);
        sslContext.init(keyMgrFact.getKeyManagers(), trustMgrFact.getTrustManagers(), null);
        sslContext.getServerSessionContext().setSessionTimeout(1);
        sslContext.getClientSessionContext().setSessionTimeout(1);

        /*用安全连接的工厂来创建安全连接套接字*/
        SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
        SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port_no);// 创建并进入监听

        /*是否验证客户端*/
        sslServerSocket.setNeedClientAuth(clientAuth);
        if (!StringUtil.isNullOrEmpty(cipherSuit)) {
            sslServerSocket.setEnabledCipherSuites(new String[]{cipherSuit});
        }

        System.err.println("Server Listening...");
        /*接受客户端的连接*/
        SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
        System.err.println("Server received client connect");

        OutputStream out = sslSocket.getOutputStream();
        InputStream in = sslSocket.getInputStream();

        /*服务端输入*/
        BufferedReader serverInput = new BufferedReader(new InputStreamReader(System.in));


        while (true) {
            /*接收客户端输入*/
            System.err.println("Waiting client 's message...");

            String clientMsg = IOUtils.toString(in, StandardCharsets.UTF_8);

            System.err.println("Received GmSslClientTest2 Message:");
            System.err.println(clientMsg);


            /*发送服务端信息*/
            System.err.print("Input Server Send Message:");

            String serverMsg = serverInput.readLine();
            if (serverMsg.trim().equalsIgnoreCase("BYE"))
                break;

            out.write(serverMsg.getBytes(StandardCharsets.UTF_8));

        }

        in.close();
        out.close();
        serverInput.close();
        sslSocket.close();
        sslServerSocket.close();

    }

    public static void main(String[] args) throws Exception {
        System.setProperty("sansec.ssl.debug.all", "true");

        try {
            new GmSslServerTest2(KeyStores.jks_server_sm2, KeyStores.jks_server_sm2_trustStore, "88888888".toCharArray(), "88888888".toCharArray()).startSSLServer(true, 12346, "");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
