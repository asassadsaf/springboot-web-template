package com.fkp.template.modules.socket.server;

import com.fkp.template.modules.socket.util.SocketUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.net.ssl.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/10/27 14:00
 */
public class SslServerTest {

    @SneakyThrows
    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        String certsDir = System.getProperty("user.dir") + File.separator + "config" + File.separator + "certs" + File.separator;
        String serverKeyStorePath = certsDir + "pt_rsa_sign_server.pfx";
        String serverTrustKeyStorePath = certsDir + "pt_rsa_cer.cer";
        SSLServerSocketFactory sslServerSocketFactory = SocketUtils.genSslServerSocketFactory(serverKeyStorePath, "swxa@2024", serverTrustKeyStorePath);
        try (ServerSocket serverSocket = sslServerSocketFactory.createServerSocket(9000)){
            while (true){
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        BufferedInputStream byteArrayInputStream = new BufferedInputStream(socket.getInputStream());
                        byte[] buffer = new byte[1000];
                        int offset = 0;
                        int readLen = 0;
                        while ((readLen = byteArrayInputStream.read(buffer, offset, buffer.length - offset)) != -1){
                            offset += readLen;
                            System.out.println(readLen);
                            System.out.println(Arrays.toString(buffer));
                        }
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }


}
