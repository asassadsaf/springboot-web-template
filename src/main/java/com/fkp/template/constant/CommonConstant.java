package com.fkp.template.constant;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/5/13 16:25
 */
public class CommonConstant {
    private CommonConstant(){}

    public static final String HMAC_SHA256 = "ACS4-HMAC-SHA256";
    public static final String HMAC_SM3 = "ACS4-HMAC-SM3";
    public static final String BASE_URL = "/test";
    public static final String VERSION_V1 = "v1";
    public static final String OK = "OK";
    public static final String SM4_ALG = "SM4";
    public static final String AES_ALG = "AES";
    public static final String CLASS_PATH_STR = "classpath:";
    public static final String[] CERT_ALIAS = {"server_enc", "server_sign", "root"};
    public static final String PUBLIC_ZONE = "public";
    public static final String POLICY_KEY_DISABLE_OUTPUT_FORM_PLAIN = "disableOutputFormPlain";
    public static final String REQUEST_METADATA_NAME = "requestMetadata";
    public static final String KEY_ID_NAME = "keyId";

}
