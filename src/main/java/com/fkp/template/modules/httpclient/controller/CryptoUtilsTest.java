package com.fkp.template.modules.httpclient.controller;

import com.kms.util.crypto.CryptoParameter;
import com.kms.util.crypto.CryptoUtils;
import com.kms.util.crypto.exception.CryptoAlgParameterException;
import com.kms.util.crypto.exception.CryptoDataException;
import com.kms.util.crypto.exception.CryptoKeyPraseException;
import com.kms.util.crypto.exception.NoProvierException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchProviderException;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/10/25 16:55
 */
public class CryptoUtilsTest {
    public static String enc(String plainText, String pubKeyHex) throws CryptoKeyPraseException, CryptoDataException, NoSuchProviderException, CryptoAlgParameterException, DecoderException, NoProvierException {
        byte[] bytes = CryptoUtils.convertSM2PublicToAsn1(Hex.decodeHex(pubKeyHex));
        byte[] bytes1 = CryptoUtils.asyEnc(bytes, plainText.getBytes(StandardCharsets.UTF_8), new CryptoParameter("SM2"));
        return Hex.encodeHexString(CryptoUtils.convertGMSM2CipherToBC(CryptoUtils.convertSM2CipherTo0018(bytes1)));
    }

    public static void main(String[] args) throws CryptoKeyPraseException, CryptoDataException, DecoderException, NoProvierException, NoSuchProviderException, CryptoAlgParameterException {
        System.out.println(enc(args[0], args[1]));
    }
}
