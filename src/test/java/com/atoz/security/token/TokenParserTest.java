package com.atoz.security.token;

import com.atoz.error.exception.InvalidTokenException;
import com.atoz.user.entity.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

public class TokenParserTest {

    private Key signingKey;

    private TokenParser sut = new TokenParser();

    @BeforeEach
    void setUp() {
        String secretKey = "b3VyLXByb2plY3QtbmFtZS1BdG9aLWxpa2UtYmxpbmQtZm9yLWdlbmVyYXRpb24tb3VyLXByb2plY3QtbGlrZS1ibGluZC1nZW5lcmF0aW9u";
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Test
    void parseClaims_토큰에서_사용자_정보를_꺼낸다() {
        String jwt = generateToken("testUserId", Set.of(Authority.ROLE_USER), 100000L);


        Claims parsedClaims = sut.parseClaims(jwt);


        assertNotNull(parsedClaims);
        assertEquals(parsedClaims.getSubject(), "testUserId");
    }

    @Test
    void parseClaims_토큰_유효기간이_만료되면_예외가_발생한다() {
        String expiredToken = generateToken("testUserId", Set.of(Authority.ROLE_USER), -100000L);


        Throwable thrown = catchThrowable(() -> {
            sut.parseClaims(expiredToken);
        });


        assertInstanceOf(InvalidTokenException.class, thrown);
        assertEquals("만료된 토큰입니다.", thrown.getMessage());
    }

    @Test
    void parseClaims_토큰_형식이_잘못되면_예외가_발생한다() {
        String wrongToken = "wrongToken";


        Throwable thrown = catchThrowable(() -> {
            sut.parseClaims(wrongToken);
        });


        assertInstanceOf(InvalidTokenException.class, thrown);
        assertEquals("잘못된 토큰입니다.", thrown.getMessage());
    }

    private String generateToken(String userId, Set<Authority> authorities, long expirationPeriod) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("auth", authorities);

        Date now = new Date();

        return Jwts.builder()
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationPeriod))
                .compact();
    }
}
