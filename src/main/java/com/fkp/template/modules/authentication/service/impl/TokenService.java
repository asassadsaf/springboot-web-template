package com.fkp.template.modules.authentication.service.impl;

import cn.hutool.core.lang.UUID;
import com.fkp.template.core.cache.TokenCacheManage;
import com.fkp.template.modules.authentication.constant.AuthConstant;
import com.fkp.template.modules.authentication.entity.SysUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * token验证处理
 *
 *
 * @author fengkunpeng
 */
@Component
public class TokenService {
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private TokenCacheManage tokenCacheManage;

    // 令牌自定义标识
    @Value("${authentication.token.header:Authorization}")
    private String header;

    // 令牌秘钥
    @Value("${authentication.token.secret:abcdefghijklmnopqrstuvwxyz}")
    private String secret;

    // 令牌有效期（默认30分钟）
    @Value("${authentication.token.expireTime:30}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;




    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public SysUserDetails getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            try {
                Claims claims = parseToken(token);
                // 解析对应的权限以及用户信息
                String uuid = (String) claims.get(AuthConstant.JWT_CLAIMS_KEY_UUID);
                String tokenKey = getTokenKey(uuid);
                return tokenCacheManage.getIfPresent(tokenKey);
            } catch (Exception e) {
                log.error("parse token to get user login info error.", e);
            }
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(SysUserDetails loginUser) {
        if (loginUser != null && StringUtils.isNotEmpty(loginUser.getUuid())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String tokenKey = getTokenKey(token);
            tokenCacheManage.invalidate(tokenKey);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(SysUserDetails loginUser) {
        String uuid = UUID.fastUUID().toString();
        loginUser.setUuid(uuid);
        refreshToken(loginUser);
        Map<String, String> claims = new HashMap<>(2);
        claims.put(AuthConstant.JWT_CLAIMS_KEY_USERNAME, loginUser.getUsername());
        claims.put(AuthConstant.JWT_CLAIMS_KEY_UUID, uuid);
        return createToken(claims);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(SysUserDetails loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(SysUserDetails loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String tokenKey = getTokenKey(loginUser.getUuid());
        tokenCacheManage.put(tokenKey, loginUser);
    }


    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, String> claims) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .signWith(secretKey, Jwts.SIG.HS256)
                .claims(claims)
                .compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        return request.getHeader(header);
    }

    private String getTokenKey(String uuid) {
        return AuthConstant.LOGIN_TOKEN_KEY + uuid;
    }
}
