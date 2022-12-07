package com.atoz.security.token;

import com.atoz.error.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class TokenParser {

    @Value("${jwt.secret:b3VyLXByb2plY3QtbmFtZS1BdG9aLWxpa2UtYmxpbmQtZm9yLWdlbmVyYXRpb24tb3VyLXByb2plY3QtbGlrZS1ibGluZC1nZW5lcmF0aW9u}")
    private String secretKey = "b3VyLXByb2plY3QtbmFtZS1BdG9aLWxpa2UtYmxpbmQtZm9yLWdlbmVyYXRpb24tb3VyLXByb2plY3QtbGlrZS1ibGluZC1nZW5lcmF0aW9u";

    private Key generateSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims parseClaims(String jwt) {
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
}
