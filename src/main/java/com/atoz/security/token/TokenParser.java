package com.atoz.security.token;

import com.atoz.error.exception.InvalidTokenException;
import com.atoz.security.authentication.dto.TokenResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TokenParser {

    private static final String AUTHORITIES_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer";

    private final Key secretKey;

    public TokenParser(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 토큰을 검증하면서 파싱한다.
     */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException ex) {
            throw new InvalidTokenException("만료된 토큰입니다.");
        } catch (IllegalArgumentException ex) {
            throw new InvalidTokenException("토큰 값이 비었습니다.");
        } catch (Exception ex) {
            throw new InvalidTokenException("잘못된 토큰입니다.");
        }
    }

    /**
     * 토큰 값을 파싱하여 클레임에 담긴 사용자 아이디 값을 가져온다.
     */
    public String parseUserId(String token) {
        return this.parseClaims(token)
                .getSubject();
    }

    public Authentication toAuthentication(String accessToken) {
        Claims claims = this.parseClaims(accessToken);
        if (claims.get(AUTHORITIES_KEY) == null || !StringUtils.hasText(claims.get(AUTHORITIES_KEY).toString())) {
            throw new RuntimeException("유저에게 아무런 권한이 없습니다.");
        }

        Collection<? extends GrantedAuthority> authorities = Stream.of(claims.get(AUTHORITIES_KEY))
                .map(String::valueOf)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
    }

    /**
     * HTTP 요청 헤더로부터 JWT 토큰을 얻는다.
     */
    public String resolveBearerToken(String bearerToken) {
        if (!StringUtils.hasText(bearerToken)) {
            throw new InvalidTokenException("bearer 토큰이 비어있습니다.");
        }

        if (!bearerToken.startsWith(BEARER_PREFIX)) {
            throw new InvalidTokenException("bearer 토큰 형식이 잘못되었습니다.");
        }

        String jwt = bearerToken.substring(7);
        if (!StringUtils.hasText(jwt)) {
            throw new InvalidTokenException("jwt 토큰이 비어있습니다.");
        }
        return jwt;
    }
}
