package com.atoz.authentication;

import com.atoz.error.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";

    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME;

    private final Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expire-time}") long accessTime,
            @Value("${jwt.refresh-token-expire-time}") long refreshTime) {
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTime;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTime;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    /**
     * 토큰 생성
     */
    protected String createToken(String userId, Set<Authority> auth, long tokenValid) {
        Claims claims = Jwts.claims().setSubject(userId);

        claims.put(AUTHORITIES_KEY, auth);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValid))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 액세스 토큰 생성
     */
    public String createAccessToken(String userId, Set<Authority> auth) {
        return this.createToken(userId, auth, ACCESS_TOKEN_EXPIRE_TIME);
    }

    /**
     * 리프레시 토큰 생성
     */
    public String createRefreshToken(String userId, Set<Authority> auth) {
        return this.createToken(userId, auth, REFRESH_TOKEN_EXPIRE_TIME);
    }

    /**
     * 토큰 값을 파싱하여 클레임에 담긴 사용자 아이디 값을 가져온다.
     */
    public String getUserIdByToken(String token) {
        return this.parseClaims(token).getSubject();
    }

    /**
     * TokenDTO 생성
     */
    public TokenDTO createTokenDTO(String accessToken, String refreshToken) {
        return TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType(BEARER_TYPE)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = this.parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null || !StringUtils.hasText(claims.get(AUTHORITIES_KEY).toString())) {
            throw new RuntimeException("유저에게 아무런 권한이 없습니다.");
        }


        Collection<? extends GrantedAuthority> authorities = Set.of(claims.get(AUTHORITIES_KEY))
                                                                .stream()
                                                                .map(String::valueOf)
                                                                .map(SimpleGrantedAuthority::new)
                                                                .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (ExpiredJwtException ex) {
            throw new InvalidTokenException("만료된 토큰입니다.");
        } catch (IllegalArgumentException ex) {
            throw new InvalidTokenException("토큰 값이 비었습니다.");
        } catch (Exception ex) {
            throw new InvalidTokenException("잘못된 토큰입니다.");
        }
    }

    /**
     * 토큰을 파싱
     */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
