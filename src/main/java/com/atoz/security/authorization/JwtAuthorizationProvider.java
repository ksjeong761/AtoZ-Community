package com.atoz.security.authorization;

import com.atoz.error.exception.InvalidTokenException;
import com.atoz.security.token.RefreshTokenEntity;
import com.atoz.security.token.RefreshTokenMapper;
import com.atoz.security.token.TokenManager;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JwtAuthorizationProvider implements AuthorizationProvider {

    private final TokenManager tokenManager;
    private final RefreshTokenMapper refreshTokenMapper;
    private final UserDetailsService userDetailsService;

    public Authentication authorize(String accessToken) {
        tokenManager.validateToken(accessToken);

        String userId = tokenManager.parseUserId(accessToken);
        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenMapper.findTokenByKey(userId);
        if (refreshTokenEntity.isEmpty()) {
            throw new InvalidTokenException("로그아웃된 사용자입니다.");
        }

        return buildAuthentication(accessToken);
    }

    private Authentication buildAuthentication(String jwt) {
        UserDetails principal = userDetailsService.loadUserByUsername(tokenManager.parseUserId(jwt));
        String credentials = "";
        Collection<? extends GrantedAuthority> authorities = tokenManager.parseGrantedAuthorities(jwt);

        return new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
    }
}