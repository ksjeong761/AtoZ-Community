package com.atoz.security.token;

import com.atoz.error.exception.InvalidTokenException;
import com.atoz.user.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Stream;

import static com.atoz.security.SecurityConstants.AUTHORITIES_KEY;

@Component
public class TokenManagerImpl implements TokenManager {

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

    @Override
    public void validateToken(String jwt) {
        parseClaims(jwt);
    }

    private Claims parseClaims(String jwt) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(generateSigningKey())
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw new InvalidTokenException("만료된 토큰입니다.");
        } catch (IllegalArgumentException ex) {
            throw new InvalidTokenException("토큰 값이 비었습니다.");
        } catch (Exception ex) {
            throw new InvalidTokenException("잘못된 토큰입니다.");
        }
    }

    @Override
    public String parseUserId(String jwt) {
        return parseClaims(jwt).getSubject();
    }

    @Override
    public Collection<? extends GrantedAuthority> parseGrantedAuthorities(String jwt) {
        Claims claims = parseClaims(jwt);
        Object authorities = claims.get(AUTHORITIES_KEY);

        if (authorities == null) {
            throw new RuntimeException("토큰에 권한 정보가 존재하지 않습니다.");
        }
        if (!StringUtils.hasText(authorities.toString())) {
            throw new RuntimeException("사용자가 가진 권한이 없습니다.");
        }

        return Stream.of(authorities)
                .map(String::valueOf)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
