//package com.fkp.template;
//
//import cn.hutool.core.util.ByteUtil;
//import com.kms.util.crypto.CryptoUtils;
//import com.sansec.adapter.AdapterUtils;
//import com.sansec.jce.provider.SwxaProvider;
//import lombok.SneakyThrows;
//import org.apache.commons.codec.binary.Base64;
//import org.apache.commons.codec.binary.Hex;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.security.Security;
//import java.util.Arrays;
//
///**
// * @author fengkunpeng
// * @version 1.0
// * @description
// * @date 2024/8/4 15:30
// */
//public class KeyFormatConvertTest {
//
//    @BeforeAll
//    static void addProvider(){
//        Security.addProvider(new SwxaProvider());
//    }
//
//    @SneakyThrows
//    @Test
//    void sm2Convert(){
//        String asn1PriKey = "MIGIAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBG4wbAIBAQIhAIj6PCRle3Cz+b1Nv5z4vHtkIXBuPTD8be3aEpGiR5QooUQDQgAEyZDlRJ56uhcPsR0pGioop5W+xv72QEncV45/jt6T6/5dJDbfoeDUT8XDdXNUrOAx8UXF/XIAKjG4YKYi9HO5kQ==";
//        String asn1PubKey = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEyZDlRJ56uhcPsR0pGioop5W+xv72QEncV45/jt6T6/5dJDbfoeDUT8XDdXNUrOAx8UXF/XIAKjG4YKYi9HO5kQ==";
//        byte[] gmPriKey = CryptoUtils.convertSM2PrivateToGM(Base64.decodeBase64(asn1PriKey));
//        System.out.println(Base64.encodeBase64String(gmPriKey));
//        System.out.println(Arrays.toString(gmPriKey));
//        System.out.println(ByteUtil.bytesToInt(Arrays.copyOfRange(gmPriKey, 0, 4)));
//        System.out.println(gmPriKey.length);
//        byte[] gmPubKey = CryptoUtils.convertSM2PublicToGM(Base64.decodeBase64(asn1PubKey));
//        System.out.println(Base64.encodeBase64String(gmPubKey));
//        System.out.println(ByteUtil.bytesToInt(Arrays.copyOfRange(gmPubKey, 0, 4)));
//        System.out.println(gmPubKey.length);
//        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPubKey, 4, 4 + 64)));
//        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPubKey, 4 + 64, 4 + 64 + 64)));
//
//        byte[] convertAsn1PriKeyBytes = CryptoUtils.convertSM2PrivateToAsn1(gmPriKey, gmPubKey);
//        String convertAsn1PriKey = Base64.encodeBase64String(convertAsn1PriKeyBytes);
//        System.out.println(convertAsn1PriKey);
//        byte[] convertAsn1PubKeyBytes = CryptoUtils.convertSM2PublicToAsn1(gmPubKey);
//        String convertAsn1PubKey = Base64.encodeBase64String(convertAsn1PubKeyBytes);
//        System.out.println(convertAsn1PubKey);
//        System.out.println(convertAsn1PriKey.equals(asn1PriKey));
//        System.out.println(convertAsn1PubKey.equals(asn1PubKey));
//
//    }
//
//    @SneakyThrows
//    @Test
//    void rsaConvert(){
//        String asn1PriKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCL2vdtyqvV3MnsNMRk6dEzQtH6Cuj83/Rc/1rLQ14iRGjIvrF2C3qPaeaB0P5XyqexH43Ta7CkYt0bCeyN2sOveFKCB00GLkQABrmd9iIFBvb2YWE/awLXd65xI6hKwDfXaFwLzI12NDTRIBlwSWdx8IY1eCeQ8dkUGNeNQBcSZBtz0AaXK3zanVKRRR1i7aSKGrPPVDQLvuHHxofvhuHXJAFaQIzE4yyFZ9us+LPIrYtdAAgTjdLwm52G2U+YBhbuGHoRCwt7Wm4NKklXbycqL9vXUWIUd+on/mJfAHh6eZEC40RSJYIJezDQLTx9KfR7bcOUytIChko1PmnwnArVAgMBAAECggEAC+y7tCzXwgB1ZyKIrGITaF+GL9/GRemCmHtdbn9sA1f550P7Nal85cWN1fvP9ARfkAa0vRYtlZaoa6nI9b91d/y2bEfbFW/UdHweT42xEOSrIg7jj9i8AUs/IH591wHUwRUw5ACo/y8sYbhu8tgPbfOoPd7k4ML+U2xa1LxmOzcVqbNGTL23yYJ3wEAVShqh3u+4FgnW4pCPaL2rNsBp6C1A4R10WoYYAzK5bUlQHOjtG7CwDTD+J1AKGIJcM0mSVfsjYn/oYNKp0Advw8e28W3dGI7pEnl7Al0Yze3rTT9XGJ/qOK7O/QrFHUlkLY21TEjxQm9w1uPAa7FOyaPG8QKBgQDBYWtcL2MILTRH5VFjJHYPVqm+J69w1wRTT8X8mTYFvqVy/7Zdg51mztJP1rdTPL6UgRwhVrIBWguKeJ30EdnxBIS/DNcKTbnvsyeD7k5efmUx593r/uZOibxeEDrSPI+byAJ/JUOqdnnxih/ckDSpsf4hkGSPiQc2cVmu9Ya7NwKBgQC5JH30QR3Tc/MngCm3Njl2V9XKsaJWhTNpNZ3GUwjP+WFe93pbmjhHZRiKjb8FvHJx+X6TdMssSGXcydvaEwKoxfWF+3CHSJvKpLgyqdbPNzUzFBnHBGjbw0T8p+PURpa/LENpVvuDGliO/BZ4WJ9mjK2AJPxf3v1eMKoo+ploUwKBgQCi9K79vcjcCQu3UhylSQJbppHfdZ23ntk8Q0r5szn82UyefbF1WAV5rus28B8H+3Y1uh60UVQrI2/6Pe4M0EYxfbdHL1C+lAUNYpD38gcERcqMpXEy/1ef8x1SvqZypyn1AjRoZESDDtdvnQ+AU9ys6xrVudGjyrrTO+1xK+FfPQKBgD/snaCMKyQrULU+xyh/DHAT+7k0g/yjmRCmdodXfB9y12/WuuSN5wifm02bi20Ll+hKYK3BEbWEecc12zb8gVqMny1Xl47ePwxEu2Six4xcX333Xr8cgqf+T/98aGmOxdXUDqJ8afLmUdVsrMV4s/DQecdY/dqMP6cZ3mavv/AJAoGBAKDNSARJJZnvNGE3YCFu3rgdafDZ+7eF9UlQlT41JFjN0fDr/hnI7E1qrxq5bDt+JpINE4es9i9t5IhwQIH0dJWiGcRgyfVew/Dvh11jzypbIS6G0o4fy/TXC+8x/OTIWcsZpZVSxRWV2jowd4s9QZn6hdutzzdFOu8x26C3oUs7";
//        String asn1PubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAi9r3bcqr1dzJ7DTEZOnRM0LR+gro/N/0XP9ay0NeIkRoyL6xdgt6j2nmgdD+V8qnsR+N02uwpGLdGwnsjdrDr3hSggdNBi5EAAa5nfYiBQb29mFhP2sC13eucSOoSsA312hcC8yNdjQ00SAZcElncfCGNXgnkPHZFBjXjUAXEmQbc9AGlyt82p1SkUUdYu2kihqzz1Q0C77hx8aH74bh1yQBWkCMxOMshWfbrPizyK2LXQAIE43S8JudhtlPmAYW7hh6EQsLe1puDSpJV28nKi/b11FiFHfqJ/5iXwB4enmRAuNEUiWCCXsw0C08fSn0e23DlMrSAoZKNT5p8JwK1QIDAQAB";
//        byte[] gmPriKey = AdapterUtils.adapterPriKey(Base64.decodeBase64(asn1PriKey), "RSA_GM");
//        System.out.println(Base64.encodeBase64String(gmPriKey));
////        System.out.println(Arrays.toString(gmPriKey));
//        // 模长 1024
//        System.out.println(ByteUtil.bytesToInt(Arrays.copyOfRange(gmPriKey, 0, 4)));
//        // 1412
//        System.out.println(gmPriKey.length);
//        //bits 4
//        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPriKey, 0, 4)));
//        //M 256
//        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPriKey, 4, 4 + 256)));
//        //E 256
//        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPriKey, 4 + 256, 4 + 256 + 256)));
//        //D 256
//        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPriKey, 4 + 256 + 256, 4 + 256 + 256 + 256)));
//        //p 128
//        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPriKey, 4 + 256 + 256 + 256, 4 + 256 + 256 + 256 + 128)));
//        //q 128
//        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPriKey, 4 + 256 + 256 + 256 + 128, 4 + 256 + 256 + 256 + 128 + 128)));
//        //Dp 128
//        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPriKey, 4 + 256 + 256 + 256 + 128 + 128, 4 + 256 + 256 + 256 + 128 + 128 + 128)));
//        //Dq 128
//        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPriKey, 4 + 256 + 256 + 256 + 128 + 128 + 128, 4 + 256 + 256 + 256 + 128 + 128 + 128 + 128)));
//        //i 128
//        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPriKey, 4 + 256 + 256 + 256 + 128 + 128 + 128 + 128, 4 + 256 + 256 + 256 + 128 + 128 + 128 + 128 + 128)));
//        // 1412 == gmPriKey.length
//        System.out.println(4 + 256 + 256 + 256 + 128 + 128 + 128 + 128 + 128);
//        byte[] gmPubKey = AdapterUtils.adapterPubKey(Base64.decodeBase64(asn1PubKey), "RSA_GM");
//        System.out.println(Base64.encodeBase64String(gmPubKey));
//        //模长 1024
//        System.out.println(ByteUtil.bytesToInt(Arrays.copyOfRange(gmPubKey, 0, 4)));
//        // 516
//        System.out.println(gmPubKey.length);
//        //bits 4
//        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPubKey, 0, 4)));
//        // M 256
//        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPubKey, 4, 4 + 256)));
//        // E 256
//        System.out.println(Arrays.toString(Arrays.copyOfRange(gmPubKey, 4 + 256, 4 + 256 + 256)));
//        // 516 == gmPubKey.length
//        System.out.println(4 + 256 + 256);
//
//        byte[] convertAsn1PriKeyBytes = CryptoUtils.convertRSAPrivateToAsn1(gmPriKey);
//        String convertAsn1PriKey = Base64.encodeBase64String(convertAsn1PriKeyBytes);
//        System.out.println(convertAsn1PriKey);
//        byte[] convertAsn1PubKeyBytes = CryptoUtils.convertRSAPublicToAsn1(gmPubKey);
//        String convertAsn1PubKey = Base64.encodeBase64String(convertAsn1PubKeyBytes);
//        System.out.println(convertAsn1PubKey);
//        System.out.println(convertAsn1PriKey.equals(asn1PriKey));
//        System.out.println(convertAsn1PubKey.equals(asn1PubKey));
//    }
//
//    @SneakyThrows
//    @Test
//    void test(){
//        long bytes1 = 0xffffffffL;
//        System.out.println(bytes1);
////        byte[] bytes = ByteUtil(10240);
////        System.out.println(Arrays.toString(bytes));
//    }
//
//    @SneakyThrows
//    @Test
//    void testConvertCipher(){
//        String cipher = "MHoCIQC2rmguuxW7f+cRYyYA2a3V5yLYZ8v+/aq/oMNAK9FqMwIhAI0tEWgxkdkO868eQ/3TyRuoHfueasAqz1EZ2ZrLZ7swBCDxL8bAMpNoxzrUOJZNwyEQffjhNz1e4qkXq7psrpuyzAQQBPQCiGCRwl/lHjsugp2f4g==";
//        byte[] bytes = CryptoUtils.convertSM2CipherTo0018(Base64.decodeBase64(cipher));
//        String gmCipher = Base64.encodeBase64String(bytes);
//        System.out.println(gmCipher);
//
//    }
//
//    @SneakyThrows
//    @Test
//    void testConvert(){
//        String hex = "308201AC30820151A00302010202082767970D2D9314E6300C06082A811CCF55018375050030413110300E06035504030C07534D32524F4F54310F300D060355040A0C0653414E534543310F300D060355040B0C0653414E534543310B300906035504061302434E3020170D3230303531313132323630315A180F32313230303431373132323630315A30413110300E06035504030C07534D32524F4F54310F300D060355040A0C0653414E534543310F300D060355040B0C0653414E534543310B300906035504061302434E3059301306072A8648CE3D020106082A811CCF5501822D03420004812526F14AD6D4F0ABB0546908F45958D95C9B9459FF051692ADF9374D8286328EA9A838E56D9A5BD4BA1214E121F68CF9629D544A93EF4949168D96CE59E9AAA32F302D301D0603551D0E041604142BC03C2E6A3D26AA199EC9143493201B1BAED0E9300C0603551D13040530030101FF300C06082A811CCF5501837505000347003044022054740C7711B8CA7E18B54A625A7E99B7D19C5689DEB2C3698D829A074CF066D4022073E0066F4276EB748EC72201B1A9A3EDBB5417E941925BFE8149C0FB1BD5CA00";
//        System.out.println(Base64.encodeBase64String(Hex.decodeHex(hex)));
//    }
//}
