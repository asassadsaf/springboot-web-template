package com.fkp.template.modules.xkip.service.impl;

import com.fkp.template.core.constant.CommonConstant;
import com.fkp.template.core.constant.ErrorCodeEnum;
import com.fkp.template.modules.xkip.dto.response.SimpleRestResponse;
import com.fkp.template.modules.xkip.entity.CertDigest;
import com.fkp.template.core.exception.BusinessException;
import com.fkp.template.modules.xkip.service.SystemService;
import com.fkp.template.core.util.ExecUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.util.*;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/27 14:30
 */
@Service
@Slf4j
public class SystemServiceImpl implements SystemService {

    @Value("${server.port}")
    private String port;

    @Value("${business.shellPath.getSourceIps}")
    private String getSourceIpsShellPath;

    @Value("${server.ssl.key-store-password}")
    private String keystorePwd;

    @Value("${server.ssl.key-store}")
    private String keyStorePath;

    @Value("${business.policy.path}")
    private String policyFilePath;

    public SystemServiceImpl() {
//        log.info("system service impl call construction. content: {}", this);
    }

    @Override
    public SimpleRestResponse<List<String>> queryIpWhiteList() {
        return SimpleRestResponse.success(getIpWhiteListByPort(port));
    }

    @Override
    public SimpleRestResponse<List<CertDigest>> generateCertDigest() {
        KeyStore keyStore = getServerKeyStore();
        List<CertDigest> list = generateCertDigestsFromKeyStore(keyStore);
        return SimpleRestResponse.success(list);
    }

    @Override
    public SimpleRestResponse<Map<String, Object>> queryPolicy() {
        try (FileReader reader = new FileReader(policyFilePath)){
            Yaml yaml = new Yaml();
            Map<String, Object> map = yaml.load(reader);
            return SimpleRestResponse.success(Optional.ofNullable(map).orElse(new LinkedHashMap<>()));
        } catch (Exception e) {
            log.error("Load policy file error. filePath: {}", policyFilePath);
            throw new BusinessException(ErrorCodeEnum.InternalServerError, "Load policy file error.", e);
        }
    }

    @Override
    public SimpleRestResponse<?> configPolicy(Map<String, Object> request) {
        SimpleRestResponse<Map<String, Object>> queryRes = queryPolicy();
        try (FileWriter writer = new FileWriter(policyFilePath)){
            Yaml yaml = new Yaml();
            Map<String, Object> map = queryRes.getData();
            if(request != null){
                map.putAll(request);
                yaml.dump(map, writer);
            }
            return SimpleRestResponse.success();
        } catch (Exception e) {
            log.error("Load policy file error. filePath: {}", policyFilePath);
            throw new BusinessException(ErrorCodeEnum.InternalServerError, "Load policy file error.", e);
        }
    }

    private List<CertDigest> generateCertDigestsFromKeyStore(KeyStore keyStore){
        MessageDigest digestSha256;
        MessageDigest digestSm3;
        try {
            digestSha256 = MessageDigest.getInstance("SHA256", BouncyCastleProvider.PROVIDER_NAME);
            digestSm3 = MessageDigest.getInstance("SM3", BouncyCastleProvider.PROVIDER_NAME);
        }catch (Exception e){
            throw new BusinessException(ErrorCodeEnum.InternalServerError, "Generate digest get instance error.", e);
        }
        List<CertDigest> list = new ArrayList<>();
        for (String alias : CommonConstant.CERT_ALIAS) {
            Certificate certificate;
            byte[] certBytes;
            byte[] pubKeyBytes;
            try {
                certificate = keyStore.getCertificate(alias);
                certBytes = certificate.getEncoded();
                pubKeyBytes = certificate.getPublicKey().getEncoded();
            }catch (Exception e){
                log.error("Generate digest get certificate from keystore error. alias: {}", alias, e);
                continue;
            }
            String certSha256Hex = Hex.encodeHexString(digestSha256.digest(certBytes));
            String pubKeySha256Hex = Hex.encodeHexString(digestSha256.digest(pubKeyBytes));

            String certSm3Hex = Hex.encodeHexString(digestSm3.digest(certBytes));
            String pubKeySm3Hex = Hex.encodeHexString(digestSm3.digest(pubKeyBytes));

            CertDigest certDigest = CertDigest.builder().certDigestSha256("sha256-" + certSha256Hex).certDigestSm3("sm3-" + certSm3Hex)
                    .publicKeyDigestSha256("sha256-" + pubKeySha256Hex).publicKeyDigestSm3("sm3-" + pubKeySm3Hex)
                    .alias(certificate.getType()).alias(alias).build();
            list.add(certDigest);
        }
        return list;
    }

    private KeyStore getServerKeyStore(){
        InputStream inputStream = null;
        try {
            if(StringUtils.isBlank(keyStorePath) || keyStorePath.contains(CommonConstant.CLASS_PATH_STR)){
                String path = StringUtils.substringAfter(keyStorePath, CommonConstant.CLASS_PATH_STR);
                inputStream = SystemServiceImpl.class.getClassLoader().getResourceAsStream(path);
            }else {
                inputStream = Files.newInputStream(Paths.get(keyStorePath));
            }
            KeyStore jks = KeyStore.getInstance("JKS");
            jks.load(inputStream, keystorePwd.toCharArray());
            return jks;
        }catch (Exception e){
            log.error("Load server key store error. pwd: {}", keystorePwd, e);
            throw new BusinessException(ErrorCodeEnum.InternalServerError, "Load server key store error.");
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Load key store close inputStream error.", e);
                }
            }
        }
    }

    private List<String> getIpWhiteListByPort(String port){
        String ips = ExecUtils.execShellAndGetOutput(getSourceIpsShellPath, port);
        if(StringUtils.isBlank(ips)){
            return Collections.emptyList();
        }
        String[] split = ips.split(" ");
        if(InetAddressValidator.getInstance().isValid(split[0])){
            return Arrays.asList(split);
        }else {
            throw new BusinessException(ErrorCodeEnum.InternalServerError, ips);
        }
    }
}
