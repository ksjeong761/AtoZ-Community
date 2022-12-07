package com.atoz.security.token;

import com.atoz.user.entity.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Set;

@Component
public class TokenProviderImpl implements TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";

    @Value("${jwt.secret:b3VyLXByb2plY3QtbmFtZS1BdG9aLWxpa2UtYmxpbmQtZm9yLWdlbmVyYXRpb24tb3VyLXByb2plY3QtbGlrZS1ibGluZC1nZW5lcmF0aW9u}")
    private String secretKey = "b3VyLXByb2plY3QtbmFtZS1BdG9aLWxpa2UtYmxpbmQtZm9yLWdlbmVyYXRpb24tb3VyLXByb2plY3QtbGlrZS1ibGluZC1nZW5lcmF0aW9u";
    @Value("${jwt.access-token-expire-time:1800000}")
    private long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;
    @Value("${jwt.refresh-token-expire-time:604800000}")
    private long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;

    private Key generateSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String createToken(String userId, Set<Authority> authorities, long expirationPeriod) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put(AUTHORITIES_KEY, authorities);

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationPeriod);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(generateSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public String createAccessToken(String userId, Set<Authority> authorities) {
        return this.createToken(userId, authorities, ACCESS_TOKEN_EXPIRE_TIME);
    }

    @Override
    public String createRefreshToken(String userId, Set<Authority> authorities) {
        return this.createToken(userId, authorities, REFRESH_TOKEN_EXPIRE_TIME);
    }

}
