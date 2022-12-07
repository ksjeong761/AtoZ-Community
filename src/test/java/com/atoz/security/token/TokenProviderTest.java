package com.atoz.security.token;

import com.atoz.user.entity.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenProviderTest {

    String secretKey = "b3VyLXByb2plY3QtbmFtZS1BdG9aLWxpa2UtYmxpbmQtZm9yLWdlbmVyYXRpb24tb3VyLXByb2plY3QtbGlrZS1ibGluZC1nZW5lcmF0aW9u";
    long accessTime = 30 * 60 * 1000L;
    long refreshTime = 7 * 24 * 60 * 60 * 1000L;

    private final TokenProvider sut = new TokenProviderImpl();

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

        assertTrue(gap == accessTime);
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

        assertTrue(gap == refreshTime);
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

    private Claims parseClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
