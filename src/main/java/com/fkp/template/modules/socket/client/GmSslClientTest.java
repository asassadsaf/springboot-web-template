package com.fkp.template.modules.socket.client;

import com.fkp.template.modules.socket.util.SocketUtils;
import com.sansec.jce.provider.SwxaProvider;
import com.sansec.tlcp.jsse.provider.SwxaJsseProvider;
import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.Security;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/10/27 14:48
 */
public class GmSslClientTest {
    @SneakyThrows
    public static void main(String[] args) {
//        System.setProperty("sansec.ssl.debug.all", "true");
        Security.addProvider(new SwxaProvider());
//        System.setProperty("SANSEC.SSL", "TRUE");
        Security.addProvider(new SwxaJsseProvider());
        String certsDir = System.getProperty("user.dir") + File.separator + "config" + File.separator + "certs" + File.separator + "SM2" + File.separator;
        String serverKeyStorePath = certsDir + "sm2cert.jks";
        String serverTrustKeyStorePath = certsDir + "sm2ca.cer";
        SSLSocketFactory sslSocketFactory = SocketUtils.genSslSocketFactory(null, null, null, true);
        try (SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket()){
            socket.connect(new InetSocketAddress("127.0.0.1", 9000));
            socket.startHandshake();
            // jdk8 connect success. protocol: [TLSv1, TLSv1.1, TLSv1.2]
            // jdk17 connect success. protocol: [TLSv1.2, TLSv1.3]
            System.out.println("connect success. protocol: " + Arrays.toString(socket.getEnabledProtocols()));
            try (OutputStream outputStream = socket.getOutputStream()){
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNext()){
                    String next = scanner.next();
                    if("0".equals(next)){
                        break;
                    }
                    System.out.println(next);
                    outputStream.write(next.getBytes());
                    outputStream.flush();
                }
            }
        }
    }
}
