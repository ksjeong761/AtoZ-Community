package com.atoz.security.authorization;

import com.atoz.error.exception.InvalidTokenException;
import com.atoz.security.token.RefreshTokenEntity;
import com.atoz.security.token.RefreshTokenMapper;
import com.atoz.security.token.TokenParser;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static com.atoz.security.SecurityConstants.AUTHORITIES_KEY;

@RequiredArgsConstructor
@Component
public class JwtAuthorizationProvider implements AuthorizationProvider {

    private final TokenParser tokenParser;
    private final RefreshTokenMapper refreshTokenMapper;
    private final UserDetailsService userDetailsService;

    public Authentication authorize(String jwt) {
        Claims claims = tokenParser.parseClaims(jwt);
        String userId = claims.getSubject();

        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenMapper.findTokenByKey(userId);
        if (refreshTokenEntity.isEmpty()) {
            throw new InvalidTokenException("로그아웃된 사용자입니다.");
        }
        if (!refreshTokenEntity.get().getTokenValue().equals(jwt)) {
            throw new InvalidTokenException("토큰이 일치하지 않습니다.");
        }

        return buildAuthentication(claims);
    }

    public Authentication authorize(String accessToken, String refreshToken) {
        Claims claims = tokenParser.parseClaims(accessToken);
        String userId = claims.getSubject();

        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenMapper.findTokenByKey(userId);
        if (refreshTokenEntity.isEmpty()) {
            throw new InvalidTokenException("로그아웃된 사용자입니다.");
        }
        if (!refreshTokenEntity.get().getTokenValue().equals(refreshToken)) {
            throw new InvalidTokenException("토큰이 일치하지 않습니다.");
        }

        return buildAuthentication(claims);
    }

    private Authentication buildAuthentication(Claims claims) {
        var principal = buildPrincipal(claims);
        var credentials = buildCredentials(claims);
        var authorities = buildAuthorities(claims);

        return new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
    }

    private UserDetails buildPrincipal(Claims claims) {
        return userDetailsService.loadUserByUsername(claims.getSubject());
    }

    private String buildCredentials(Claims claims) {
        return "";
    }

    private Collection<? extends GrantedAuthority> buildAuthorities(Claims claims) {
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