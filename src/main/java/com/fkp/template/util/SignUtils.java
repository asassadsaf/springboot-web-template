package com.fkp.template.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import com.fkp.template.constant.ErrorCodeEnum;
import com.fkp.template.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.engines.SM2Engine;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/2 16:45
 */
@Slf4j
public class SignUtils {

    //时间戳
    public static final String TIMESTAMP_KEY = "timeStamp";

    //随机字符串
    public static final String RAND_KEY = "randStr";

    //签名值
    public static final String SIGN_KEY = "sign";

    //过期时间，2分钟
    private static final Long EXPIRE_TIME = 2 * 60L;

    private static String SM2_PRIVATEKEY = "2d06e738e864ba5e384da84eb3156516600b87644a9019600944203d5ad7be23";
    private static String SM2_PUBLICKEY = "0457beded4312c0293cb78ec15b0f50ea47c49802e9dcd4547383fd3585b82ca2088bb5fa0796c28542710e20e78291376b0748f08e5eb76c2f6f5119d8ecca225";

    private static SM2 sm2 = null;

    static {
        sm2 = SmUtil.sm2(SM2_PRIVATEKEY, SM2_PUBLICKEY);
        sm2.setMode(SM2Engine.Mode.C1C3C2);
    }

    /**
     * 签名方法
     * 1.加入时间戳和随机字符串参数
     * 2.所有key按字典序排序
     * 3.如果value是对象或数组，转换成json字符串
     * 4.过滤掉所有value为空的字段
     * 5.将key和value进行拼接，最后加上密钥key，规则：key1=value1&key2=value2 ...
     * 6.将4得到的字符串进行MD5加密，然后转换成大写，最终生成即为sign的值
     *
     * 例如：
     * {
     *     "k3": {
     *         "k4": "v4",
     *         "k5": "v5"
     *     },
     *     "k6": [
     *         {
     *             "k7": "v7",
     *             "k8": 8
     *         }
     *     ],
     *     "k9": 9,
     *     "k2": "v2",
     *     "k1": "v1"
     * }
     *
     * 拼接字符示例：k1=v1&k2=v2&k3={"k4":"v4","k5":"v5"}&k6=[{"k7":"v7","k8":8}]&k9=9&randStr=0.8244844229922232&timeStamp=162555624011&key=xxx
     *
     * @param map 传TreeMap，为了兼容后期可能对整个body进行加签操作
     * @return
     */
    public static String sign(TreeMap<String, String> map) {
        String sign = sm2.encryptBase64(getPreSignContent(map), KeyType.PublicKey);
        if (!map.containsKey(SIGN_KEY)) {
            map.put(SIGN_KEY, sign);
        }
        return sign;
    }

    private static String getPreSignContent(TreeMap<String, String> map){
        if (!map.containsKey(TIMESTAMP_KEY)) {
            map.put(TIMESTAMP_KEY, String.valueOf(System.currentTimeMillis() / 1000));
        }
        if (!map.containsKey(RAND_KEY)) {
            map.put(RAND_KEY, String.valueOf(ThreadLocalRandom.current().nextDouble()));
        }
        StringBuilder buf = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!SIGN_KEY.equals(entry.getKey()) && StrUtil.isNotBlank(entry.getValue())) {
                buf.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }

        return buf.substring(1);
    }

    /**
     * 数据验签
     * @param map 传TreeMap，为了兼容后期可能对整个body进行验签操作
     */
    public static void verify(TreeMap<String, String> map) {
        if (StrUtil.isBlank(map.get(TIMESTAMP_KEY))
                || StrUtil.isBlank(map.get(RAND_KEY))
                || StrUtil.isBlank(map.get(SIGN_KEY))) {
            throw new BusinessException(ErrorCodeEnum.InternalServerError, "get parameters from header error.");
        }
        String decrypt = StrUtil.utf8Str(sm2.decryptStr(map.get(SIGN_KEY), KeyType.PrivateKey));
        if (!StringUtils.equals(decrypt, getPreSignContent(map))){
            throw new BusinessException(ErrorCodeEnum.InternalServerError, "request body verify error.");
        }
    }


}
