package com.atoz.security.token;

import com.atoz.error.exception.InvalidTokenException;
import com.atoz.user.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenManagerTest {

    private final String secretKey = "b3VyLXByb2plY3QtbmFtZS1BdG9aLWxpa2UtYmxpbmQtZm9yLWdlbmVyYXRpb24tb3VyLXByb2plY3QtbGlrZS1ibGluZC1nZW5lcmF0aW9u";
    private final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;
    private final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;
    private Key signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

    private final TokenManager sut = new TokenManagerImpl();

    @Test
    void createAccessToken_비밀키로_암호화_된_액세스_토큰을_생성한다() {
        String userId = "testUserId";


        String accessToken = sut.createAccessToken(userId, null);


        assertThrows(UnsupportedJwtException.class, () -> {
            Jwts.parserBuilder()
                    .build()
                    .parseClaimsJwt(accessToken)
                    .getBody()
                    .getSubject();
        });
    }

    @Test
    void createAccessToken_사용자_아이디로_액세스_토큰을_생성한다() {
        String userId = "testUserId";


        String accessToken = sut.createAccessToken(userId, null);


        Claims claims = parseClaims(accessToken);
        assertNotNull(claims);
        assertEquals(claims.getSubject(), userId);
    }

    @Test
    void createAccessToken_발행일자를_사용해서_액세스_토큰을_생성한다() {
        String userId = "testUserId";


        String accessToken = sut.createAccessToken(userId, null);


        Claims claims = parseClaims(accessToken);
        assertNotNull(claims);
        assertNotNull(claims.getIssuedAt());
    }

    @Test
    void createAccessToken_만료시간이_30분인_액세스_토큰을_생성한다() {
        String userId = "testUserId";


        String accessToken = sut.createAccessToken(userId, null);


        Claims claims = parseClaims(accessToken);
        Date expiration = claims.getExpiration();
        Date issuedAt = claims.getIssuedAt();
        long gap = expiration.getTime() - issuedAt.getTime();

        assertTrue(gap == ACCESS_TOKEN_EXPIRE_TIME);
    }

    @Test
    void createAccessToken_권한_정보를_가진_액세스_토큰을_생성한다() {
        String userId = "testUserId";
        Set<Authority> authorities = Set.of(Authority.ROLE_USER);


        String accessToken = sut.createAccessToken(userId, authorities);


        Claims claims = parseClaims(accessToken);
        Set<Authority> parsedAuthorities = Stream.of(claims.get("auth"))
                .map(String::valueOf)
                .map(str -> str.replaceAll("\\[", ""))
                .map(str -> str.replaceAll("\\]", ""))
                .map(Authority::valueOf)
                .collect(Collectors.toSet());

        assertEquals(1, parsedAuthorities.size());
        assertTrue(parsedAuthorities.contains(Authority.ROLE_USER));
    }
    @Test
    void createRefreshToken_비밀키로_암호화_된_리프레시_토큰을_생성한다() {
        String userId = "testUserId";


        String refreshToken = sut.createRefreshToken(userId, null);


        assertThrows(UnsupportedJwtException.class, () -> {
            Jwts.parserBuilder()
                    .build()
                    .parseClaimsJwt(refreshToken)
                    .getBody()
                    .getSubject();
        });
    }

    @Test
    void createRefreshToken_사용자_아이디로_리프레시_토큰을_생성한다() {
        String userId = "testUserId";


        String refreshToken = sut.createRefreshToken(userId, null);


        Claims claims = parseClaims(refreshToken);
        assertNotNull(claims);
        assertEquals(claims.getSubject(), userId);
    }

    @Test
    void createRefreshToken_발행일자를_사용해서_리프레시_토큰을_생성한다() {
        String userId = "testUserId";


        String refreshToken = sut.createRefreshToken(userId, null);


        Claims claims = parseClaims(refreshToken);
        assertNotNull(claims);
        assertNotNull(claims.getIssuedAt());
    }

    @Test
    void createRefreshToken_만료시간이_1주일인_리프레시_토큰을_생성한다() {
        String userId = "testUserId";


        String refreshToken = sut.createRefreshToken(userId, null);


        Claims claims = parseClaims(refreshToken);
        Date expiration = claims.getExpiration();
        Date issuedAt = claims.getIssuedAt();
        long gap = expiration.getTime() - issuedAt.getTime();

        assertTrue(gap == REFRESH_TOKEN_EXPIRE_TIME);
    }


    @Test
    void createRefreshToken_권한_정보를_가진_리프레시_토큰을_생성한다() {
        String userId = "testUserId";
        Set<Authority> authorities = Set.of(Authority.ROLE_USER);


        String refreshToken = sut.createRefreshToken(userId, authorities);


        Claims claims = parseClaims(refreshToken);
        Set<Authority> parsedAuthorities = Stream.of(claims.get("auth"))
                .map(String::valueOf)
                .map(str -> str.replaceAll("\\[", ""))
                .map(str -> str.replaceAll("\\]", ""))
                .map(Authority::valueOf)
                .collect(Collectors.toSet());

        assertEquals(1, parsedAuthorities.size());
        assertTrue(parsedAuthorities.contains(Authority.ROLE_USER));
    }


    @Test
    void parseClaims_토큰에서_사용자_정보를_꺼낸다() {
        String jwt = generateToken("testUserId", Set.of(Authority.ROLE_USER), 100000L);


        String userId = sut.parseUserId(jwt);


        assertEquals(userId, "testUserId");
    }

    @Test
    void parseClaims_토큰_유효기간이_만료되면_예외가_발생한다() {
        String expiredToken = generateToken("testUserId", Set.of(Authority.ROLE_USER), -100000L);


        Throwable thrown = catchThrowable(() -> {
            sut.validateToken(expiredToken);
        });


        assertInstanceOf(InvalidTokenException.class, thrown);
        assertEquals("만료된 토큰입니다.", thrown.getMessage());
    }

    @Test
    void parseClaims_토큰_형식이_잘못되면_예외가_발생한다() {
        String wrongToken = "wrongToken";


        Throwable thrown = catchThrowable(() -> {
            sut.validateToken(wrongToken);
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

    private Claims parseClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
