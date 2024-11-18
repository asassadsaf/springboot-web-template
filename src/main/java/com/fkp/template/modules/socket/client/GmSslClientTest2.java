package com.fkp.template.modules.socket.client;

import com.fkp.template.modules.socket.util.KeyStores;
import com.sansec.JSSEVersion;
import com.sansec.jce.provider.SwxaProvider;
import com.sansec.tlcp.jsse.provider.SwxaJsseProvider;
import io.netty.util.internal.StringUtil;
import org.apache.commons.io.IOUtils;

import javax.net.ssl.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.Security;


public class GmSslClientTest2 {
    private static String ProtocalVersion = "TLCPv1.1";
    private static final String PROVIDER = "SwxaJSSE";
    private static final String ALGRITHM = "SwxaX509";      //SwxaX509 X509  PKI
    private static String KS_TYPE = "JKS";                 //SWKS JKS

    /*客户端Key Store*/
    byte[] clientKeyStoreDate;
    byte[] clientTrustStoreDate;

    char[] keyFilePass;
    char[] keyPass;// 密钥别名的密码
    // 客户端是否传入证书
    boolean inputClientJks = false;

    static {
        Security.addProvider(new SwxaProvider(null,null));
    }

    public GmSslClientTest2(byte[] clientKeyStoreDate, byte[] clientTrustStoreDate, char[] keyFilePass, char[] keyPass, boolean inputClientJks) {
        this.clientKeyStoreDate = clientKeyStoreDate;
        this.clientTrustStoreDate = clientTrustStoreDate;
        this.keyFilePass = keyFilePass;
        this.keyPass = keyPass;
        this.inputClientJks = inputClientJks;
    }

    public void startSSLClient(String host, int port_no,String cipherSuit) throws Exception {

        /* JCE Provider信息*/
        SwxaProvider swxaJce = new SwxaProvider(null, null);
        System.setProperty("SANSEC.SSL", "TRUE");

        //添加jsse provider
        Security.addProvider(new SwxaJsseProvider());

        System.err.println(JSSEVersion.getVersion());

        /*客户端KeyStore*/
        KeyStore clientKeyStore = KeyStore.getInstance(KS_TYPE, swxaJce);
        clientKeyStore.load(new ByteArrayInputStream(clientKeyStoreDate), keyFilePass);
        System.err.println("1");

        /*客户端TrustKeyStore*/
        KeyStore clientTrustKeyStore = KeyStore.getInstance(KS_TYPE, swxaJce);
        clientTrustKeyStore.load(new ByteArrayInputStream(clientTrustStoreDate), keyFilePass);

        System.err.println("2");
        /*密钥管理工厂*/
        KeyManagerFactory clientMgrFact = KeyManagerFactory.getInstance(ALGRITHM, PROVIDER);
        clientMgrFact.init(clientKeyStore, keyPass);
        System.err.println("3");
        /*信任密钥工厂*/
        TrustManagerFactory trustMgrFact = TrustManagerFactory.getInstance("PKIX", PROVIDER);
        trustMgrFact.init(clientTrustKeyStore);
        System.err.println("4");
        /*SSL上下文*/
        SSLContext clientContext = SSLContext.getInstance(ProtocalVersion, PROVIDER);
        if (inputClientJks) {
            clientContext.init(null, trustMgrFact.getTrustManagers(), null);
        }else {
            clientContext.init(clientMgrFact.getKeyManagers(), trustMgrFact.getTrustManagers(), null);
        }
        System.err.println("5");
        /*创建Socket*/
        SSLSocketFactory sslSocketFactory = clientContext.getSocketFactory();
        SSLSocket cSock = (SSLSocket) sslSocketFactory.createSocket(host, port_no);

        if (!StringUtil.isNullOrEmpty(cipherSuit)) {
            cSock.setEnabledCipherSuites(new String[]{cipherSuit});
        }


        System.err.println("6");

        OutputStream out = cSock.getOutputStream();
        InputStream in = cSock.getInputStream();

        /*客户端发送输入的信息*/
        BufferedReader clientInput = new BufferedReader(new InputStreamReader(System.in));


        System.err.println("GmSslClientTest2 receive Size:" + cSock.getReceiveBufferSize());
        System.err.println("GmSslClientTest2 send Size:" + cSock.getSendBufferSize());

        while (true) {
            /*发送客户端数据*/
            System.err.print("Input GmSslClientTest2 Send Message:");
            String clientMsg = clientInput.readLine();
            out.write(clientMsg.getBytes(StandardCharsets.UTF_8));

            if ("BYE".equalsIgnoreCase(clientMsg.trim()))
                break;
            else {
                System.err.println("Waiting Server Message..");
            }

            /*接收服务端数据*/
            String serverMsg = IOUtils.toString(in, StandardCharsets.UTF_8);
            System.err.println("Received Server Message:");
            System.err.println(serverMsg);

            if (serverMsg.trim().equalsIgnoreCase("BYE"))
                break;
        }

        in.close();
        out.close();
        clientInput.close();
        cSock.close();
    }

    public static void main(String[] args) {
        System.setProperty("sansec.ssl.debug.all", "true");

        try {
            new GmSslClientTest2(KeyStores.jks_server_sm2, KeyStores.jks_server_sm2_trustStore, "88888888".toCharArray(), "88888888".toCharArray(),false).startSSLClient("127.0.0.1", 12346, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
