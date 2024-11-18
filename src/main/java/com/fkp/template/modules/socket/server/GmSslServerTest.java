package com.fkp.template.modules.socket.server;

import com.fkp.template.modules.socket.util.SocketUtils;
import com.sansec.jce.provider.SwxaProvider;
import com.sansec.tlcp.jsse.provider.SwxaJsseProvider;
import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Security;
import java.util.Arrays;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/10/27 14:00
 */
public class GmSslServerTest {

    @SneakyThrows
    public static void main(String[] args) {
//        System.setProperty("sansec.ssl.debug.all", "true");
        Security.addProvider(new SwxaProvider());
//        System.setProperty("SANSEC.SSL", "TRUE");
        Security.addProvider(new SwxaJsseProvider());
        String certsDir = System.getProperty("user.dir") + File.separator + "config" + File.separator + "certs" + File.separator + "ccsp-x86" + File.separator;
        String serverKeyStorePath = certsDir + "sm2_ccsp.jks";
        String serverTrustKeyStorePath = certsDir + "sm2cert.jks";
        SSLServerSocketFactory sslServerSocketFactory = SocketUtils.genSslServerSocketFactory(serverKeyStorePath, "swxa1234.", null, true);
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
