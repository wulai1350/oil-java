package com.rzico.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtToken生成的工具类
 * JWT token的格式：header.payload.signature
 * header的格式（算法、token的类型）：
 * {"alg": "HS512","typ": "JWT"}
 * payload的格式（用户名、创建时间、生成时间）：
 * {"sub":"wang","created":1489079981393,"exp":1489684781}
 * signature的生成算法：
 * HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 * Created by macro on 2018/4/26.
 */

@Component
public class JwtTokenUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String CLAIM_KEY_USERID= "userId";
    private static final String CLAIM_KEY_USERNAME = "username";
    private static final String CLAIM_KEY_MCHID = "mchId";
    private static final String CLAIM_KEY_NICKNAME = "nickname";
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.tokenSecret}")
    private String tokenSecret;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

    /**
     * 根据负责生成JWT的token
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    /**
     * 从token中获取JWT中的负载
     */
    private Claims getClaimsFromToken(String token) {
        if (token.startsWith(tokenPrefix)){
            token = token.substring(6);
        }
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(tokenSecret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            LOGGER.info("JWT格式验证失败:{}",token);
        }
        return claims;
    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中获取登录用户名
     */
    public String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.get(CLAIM_KEY_USERNAME).toString();
//            username =  claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 从token中获取登录用户名
     */
    public Map<String,String> getUserInfoFromToken(String token) {
        Map<String,String> sysUser = new HashMap<>();
        // The part after "Bearer", 没有空格
        Claims claims = getClaimsFromToken(token);
        if (claims!=null) {
            sysUser.put("id", claims.get(CLAIM_KEY_USERID).toString());
            sysUser.put("username", claims.get(CLAIM_KEY_USERNAME).toString());
            sysUser.put("nickname", String.valueOf(claims.get(CLAIM_KEY_NICKNAME)));
            sysUser.put("mchId", String.valueOf(claims.get(CLAIM_KEY_MCHID)));
        }
        return sysUser;
    }
    /**
     * 验证token是否还有效
     *
     * @param token       客户端传入的token
     * @param userDetails 从数据库中查询出来的用户信息
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否已经失效
     */
    public boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据用户信息生成token
     */
    public String generateToken(UserDetails userDetails) {
        Assert.notNull(userDetails, "jwt user 不能为null");
        JwtUserDetails jwtUserDetails = (JwtUserDetails) userDetails;
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put(CLAIM_KEY_USERID, jwtUserDetails.getUserId());
        claims.put(CLAIM_KEY_USERNAME, jwtUserDetails.getUsername());
        claims.put(CLAIM_KEY_NICKNAME, jwtUserDetails.getNickname());
        claims.put(CLAIM_KEY_MCHID, jwtUserDetails.getMchId());
        return generateToken(claims);
    }

    /**
     * 判断token是否可以被刷新
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }
}
