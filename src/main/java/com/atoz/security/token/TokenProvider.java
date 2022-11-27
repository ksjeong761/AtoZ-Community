package com.atoz.security.token;

import com.atoz.user.entity.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Getter
@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";

    private final Key secretKey;
    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME;

    public TokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expire-time}") long accessTime,
            @Value("${jwt.refresh-token-expire-time}") long refreshTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTime;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTime;
    }

    /**
     * 권한 정보를 지닌 JWT 토큰을 생성한다.
     */
    protected String createToken(String userId, Set<Authority> authorities, long expirationPeriod) {
        // 사용자 권한 정보를 토큰에 저장한다.
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put(AUTHORITIES_KEY, authorities);

        // 토큰 만료 시간을 토큰에 저장한다.
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationPeriod);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 만료 시간이 짧은 액세스 토큰 생성
     */
    public String createAccessToken(String userId, Set<Authority> auth) {
        return this.createToken(userId, auth, ACCESS_TOKEN_EXPIRE_TIME);
    }

    /**
     * 만료 시간이 긴 리프레시 토큰 생성
     */
    public String createRefreshToken(String userId, Set<Authority> auth) {
        return this.createToken(userId, auth, REFRESH_TOKEN_EXPIRE_TIME);
    }
}
