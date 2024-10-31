package com.fkp.template.modules.socket.client;

import com.fkp.template.modules.socket.util.SocketUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/10/27 14:48
 */
public class SslClientTest {
    @SneakyThrows
    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        String certsDir = System.getProperty("user.dir") + File.separator + "config" + File.separator + "certs" + File.separator;
        String serverKeyStorePath = certsDir + "pt_rsa_sign_client.pfx";
        String serverTrustKeyStorePath = certsDir + "rsa2048-sign.cer";
        SSLSocketFactory sslSocketFactory = SocketUtils.genSslSocketFactory(serverKeyStorePath, "swxa@2024", serverTrustKeyStorePath);
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
