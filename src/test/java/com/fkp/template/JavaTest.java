package com.fkp.template;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.fkp.template.core.constant.AlgorithmEnum;
import com.fkp.template.modules.app.entity.SysApp;
import com.fkp.template.modules.xkip.dto.request.GenRandomRequest;
import com.kms.util.crypto.CryptoParameter;
import com.kms.util.crypto.CryptoUtils;
import com.kms.util.crypto.HexUtils;
import com.kms.util.crypto.exception.CryptoAlgParameterException;
import com.kms.util.crypto.exception.CryptoDataException;
import com.kms.util.crypto.exception.CryptoKeyPraseException;
import com.sansec.jce.provider.SwxaProvider;
import lombok.SneakyThrows;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;
import org.springframework.util.Base64Utils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/23 19:54
 */
public class JavaTest {

    //不在循环中，创建多个匿名类，其Class名不同class com.fkp.template.JavaTest$1/class com.fkp.template.JavaTest$2...
    @Test
    void testInnerClass() {
        Class<? extends Object> aClass = new Object() {
            private String str;
        }.getClass();
        Class<? extends Object> aClass2 = new Object() {
            private String str;
        }.getClass();
        Class<? extends Object> aClass3 = new Object() {
            private String str;
        }.getClass();
        System.out.println(aClass);
        System.out.println(aClass2);
        System.out.println(aClass3);
    }

    //在循环中，创建多个匿名类，其Class名相同
    @Test
    void testInnerClassForLoop(){
        List<Class<? extends Object>> list = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            Class<? extends Object> aClass = new Object() {
                private int index = finalI;
            }.getClass();
            list.add(aClass);
        }
        list.forEach(System.out::println);
    }

    @Test
    void test(){
        GenRandomRequest build = GenRandomRequest.builder().build();
        System.out.println(build);
    }

    @Test
    void test6(){
        System.out.println(10_000);
    }

    @Test
    void test2() throws IOException, NoSuchAlgorithmException, NoSuchProviderException, CertificateException {
        Security.addProvider(new BouncyCastleProvider());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\fengkunpeng\\Desktop\\cert.cer");
        byte[] bytes = new byte[1024];
        int len;
        while ((len = fileInputStream.read(bytes)) != -1){
            bos.write(bytes, 0, len);
        }
        String s = bos.toString(StandardCharsets.UTF_8.name());
        String s1 = s.replaceAll("-----BEGIN CERTIFICATE-----", "")
                .replace("-----END CERTIFICATE-----", "")
                .replaceAll("\\s", "");
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64Utils.decodeFromString(s1));
        byte[] encoded = x509EncodedKeySpec.getEncoded();

        System.out.println(Base64Utils.encodeToString(encoded));

        MessageDigest digest = MessageDigest.getInstance("SHA256");
        byte[] result = digest.digest(encoded);
        System.out.println(Hex.encodeHexString(result));

        MessageDigest digest2 = MessageDigest.getInstance("SM3");
        byte[] result2 = digest2.digest(encoded);
        System.out.println(Hex.encodeHexString(result2));

        CertificateFactory instance = CertificateFactory.getInstance("X.509");
        Certificate certificate = instance.generateCertificate(new ByteArrayInputStream(Base64Utils.decodeFromString(s1)));
        PublicKey publicKey = certificate.getPublicKey();
        byte[] encoded1 = publicKey.getEncoded();
        byte[] result3 = digest.digest(encoded1);
        System.out.println(Hex.encodeHexString(result3));

        byte[] result4 = digest2.digest(encoded1);
        System.out.println(Hex.encodeHexString(result4));

//        openssl x509 -in nginx-cert.pem -outform der | openssl dgst -sha256 5165ef8cd5babac0957c7ac4ffa0b4d15de73f7a1d892b73e416e4e153633b3c
//        openssl x509 -in nginx-cert.pem -noout -pubkey | openssl base64 -d | openssl dgst -sm3

    }

    @SneakyThrows
    @Test
    void test3() {
        String[] certAlias = {"server_enc", "server_sign", "root"};
        Security.addProvider(new BouncyCastleProvider());
        KeyStore jks = KeyStore.getInstance("JKS");
//        InputStream resourceAsStream = JavaTest.class.getClassLoader().getResourceAsStream("rsa_server.jks");
        FileInputStream fileInputStream = new FileInputStream("D:\\IDEAWorkSpace\\src\\main\\resources\\rsa_server.jks");
        jks.load(fileInputStream, "swxa@2024".toCharArray());
        for (String alias : certAlias) {
            Certificate cert = jks.getCertificate(alias);
            byte[] certBytes = cert.getEncoded();
            byte[] pubKeyBytes = cert.getPublicKey().getEncoded();
            MessageDigest digestSha256 = MessageDigest.getInstance("SHA256");
            byte[] certSha256 = digestSha256.digest(certBytes);
            System.out.println(Hex.encodeHexString(certSha256));

            byte[] pubKeySha256 = digestSha256.digest(pubKeyBytes);
            System.out.println(Hex.encodeHexString(pubKeySha256));

            MessageDigest digestSm3 = MessageDigest.getInstance("SM3");
            byte[] certSm3 = digestSm3.digest(certBytes);
            System.out.println(Hex.encodeHexString(certSm3));

            byte[] pubKeySm3 = digestSm3.digest(pubKeyBytes);
            System.out.println(Hex.encodeHexString(pubKeySm3));
        }

    }

    @SneakyThrows
    @Test
    void testYml(){
        FileReader reader = new FileReader("C:\\Users\\fengkunpeng\\Desktop\\policy.yml");
        Yaml yaml = new Yaml();
        Map<String, Object> load = yaml.load(reader);
        reader.close();
        System.out.println(load);
        Map<String, Object> map = new LinkedHashMap<>();

        load.put("disableOutputFormPlain", false);
//        load.putAll(Collections.singletonMap("disableOutputFormPlain", true));
        FileWriter writer = new FileWriter("C:\\Users\\fengkunpeng\\Desktop\\policy.yml");
        yaml.dump(load, writer);
        writer.close();
    }

    @SneakyThrows
    @Test
    void test5(){
//        byte[] iv16 = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
//        byte[] iv12 = {0,1,2,3,4,5,6,7,8,9,10,11};
//        System.out.println(Base64Utils.encodeToString(iv16));
//        System.out.println(Base64Utils.encodeToString(iv12));
        String SM4KeyStr = "3b11b7702a0b8a356c32598a";
        byte[] bytes = Hex.decodeHex(SM4KeyStr);
        System.out.println(bytes.length);
//        System.out.println(new String(bytes));
        System.out.println(Base64Utils.encodeToString(bytes));
        String cipherText = "xv7xbtmK11db5TL0nQrpVvN12OBNERKkZBuxHA==";
        System.out.println(Hex.encodeHexString(Base64Utils.decodeFromString(cipherText)));
    }

    @Test
    void testEnum(){
        AlgorithmEnum.valueOf(null);
    }

    @Test
    void testJdkVersion(){
        System.out.println(System.getProperty("java.version"));
    }

    @SneakyThrows
    @Test
    void testConvertLowerCase(){
        File dir = new File("C:\\Users\\fengkunpeng\\Desktop\\证券4.1.2\\chunjun\\task\\OpenGaussToOpenGauss_Dynamics\\json\\plat");
        File[] files = dir.listFiles();
        for (File file : files) {
            convertChunjunJson2LowerCase(file, file, true, "postgresqlsource", "postgresqlsink");
            convertChunjunJson2LowerCase(file, file, false, "postgresqlsource", "postgresqlsink");
        }
    }

    @SneakyThrows
    @Test
    void convertChunjunJson2LowerCase(File sourceFile, File targetFile, boolean isReader, String sourceName, String sinkName){
        String jsonStr = FileUtils.readFileToString(sourceFile, StandardCharsets.UTF_8);
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        JSONObject readerObj = jsonObject.getJSONObject("job").getJSONArray("content").getJSONObject(0).getJSONObject(isReader ? "reader" : "writer");
        readerObj.put("name", isReader ? sourceName : sinkName);
        JSONObject parameterObj = readerObj.getJSONObject("parameter");
        JSONArray columnArray = parameterObj.getJSONArray("column");
        for (Object obj : columnArray) {
            JSONObject columnObj = (JSONObject) obj;
            columnObj.put("name", columnObj.getString("name").toLowerCase(Locale.ENGLISH));
        }
        if(isReader){
            parameterObj.put("increColumn", "version");
        }
        JSONArray tableArray = parameterObj.getJSONArray("connection").getJSONObject(0).getJSONArray("table");
        tableArray.add(tableArray.remove(0).toString().toLowerCase(Locale.ENGLISH));
        if(!isReader){
            JSONArray uniqueKeyArray = parameterObj.getJSONArray("uniqueKey");
            if(CollectionUtils.isNotEmpty(uniqueKeyArray)){
                uniqueKeyArray.add(uniqueKeyArray.remove(0).toString().toLowerCase(Locale.ENGLISH));
            }
            parameterObj.put("allReplace", true);
        }
        String jsonString = jsonObject.toJSONString(JSONWriter.Feature.PrettyFormat);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        targetFile.createNewFile();
        FileUtils.writeStringToFile(targetFile, jsonString, StandardCharsets.UTF_8);
        System.out.println("write file" + (isReader ? "reader" : "writer") + "part success.targetFilePath: " + targetFile.getAbsolutePath());
    }

    @Test
    void testProperty(){
        // Properties继承HashTable，其不能有null的key和value，ConcurrentHashMap不能有null的key和value
        Properties properties = new Properties();
        // NullPointerException
//        properties.setProperty("abc", null);
        // NullPointerException
//        properties.setProperty(null, "fkp");
        System.out.println(properties);
//         HashMap可以有null值，key也可以为null，但是只能有一个为null的key
        Map<String, Object> map = new HashMap<>();
        map.put("abc", null);
        System.out.println(map);
    }

    @Test
    void testConvertType(){
        Long longValue = 3L;
        Integer integerValue = 3;
        String strValue = "3";

        System.out.println(longValue.toString().getClass().getName());
        System.out.println(integerValue.toString().getClass().getName());
        System.out.println(strValue.getClass().getName());
    }

    @Test
    void testOptional(){
        List<SysApp> list = null;
        Random random = new Random();
        int i = random.nextInt(100);
        if(i< 5){
            list = new ArrayList<>();
            list.add(SysApp.builder().name("fkp").build());
        }
        List<String> collect = Optional.ofNullable(list).orElse(Collections.emptyList()).stream().map(SysApp::getName).collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    void testRemoveAll(){
        List<String> list = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        list.add("fkp");
        list.removeAll(list2);
        System.out.println(list);
    }

    @SneakyThrows
    @Test
    void testfkp(){
        String key = "d2qV+1Jg2MfW6UBWrTT3iMdMfiidYJ096byzzz29iFM=";
        System.out.println(Base64.decodeBase64(key).length);
        String keyHex = "2A7ECBF0364AFBEAC252F644248A6953D7018B7FE0E8375B6262FE219A29FE0960C9CCE43D1EFE5E294C4FB6181045FF";
        String s = Base64.encodeBase64String(Hex.decodeHex(keyHex));
        System.out.println(s);
        System.out.println(Base64.decodeBase64("Kn7L8DZK++rCUvZEJIppU9cBi3/g6DdbYmL+IZop/glgyczkPR7+XilMT7YYEEX/").length);
    }

    @Test
    void testDec() throws CryptoKeyPraseException, CryptoDataException, NoSuchProviderException, CryptoAlgParameterException {
        Security.addProvider(new SwxaProvider());
        byte[] keyIdBytes = "Symm57461be5-010f-4581-82bd-ee4fad2c4e19".getBytes(StandardCharsets.UTF_8);
        byte[] iv = HexUtils.subbytes(keyIdBytes, keyIdBytes.length - 16, 16);
        CryptoParameter cryptoParameter = new CryptoParameter("SM4", "CBC", "PKCS5PADDING");
        cryptoParameter.setIv(iv);
        System.out.println(Arrays.toString(iv));
        String s = CryptoUtils.symInnerDec(1, "6ZQY7Nqoklx5xtPZRShvVwkw30p3k2kW+bjwIm34fOxgy9V7q+LJPRR6gDUHbx/+", cryptoParameter);
        System.out.println(s);
    }

    @Test
    void testfkp2() throws DecoderException {
        String key = "b/g0lpLTYPueubWVyHYO3cdMfiidYJ096byzzz29iFM=";
        System.out.println(Base64.decodeBase64(key).length);

        String encKey = "E99418ECDAA8925C79C6D3D945286F570930DF4A77936916F9B8F0226DF87CEC60CBD57BABE2C93D147A8035076F1FFE";
        System.out.println(Hex.decodeHex(encKey).length);
        System.out.println(Base64.encodeBase64String(Hex.decodeHex(encKey)));
    }

    @Test
    void testNormalPath(){
        String pathStr = "/./home";
        String pathStr2 = "/opt/../home";
        Path path = Paths.get(pathStr2).normalize();
        System.out.println(path.toString());
    }
}
