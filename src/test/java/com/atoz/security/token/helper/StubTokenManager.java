package com.atoz.security.token.helper;

import com.atoz.security.token.TokenManager;
import com.atoz.user.entity.Authority;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

public class StubTokenManager implements TokenManager {
    @Override
    public String createAccessToken(String userId, Set<Authority> authorities) {
        return userId;
    }

    @Override
    public String createRefreshToken(String userId, Set<Authority> authorities) {
        return userId;
    }

    @Override
    public void validateToken(String token) {

    }

    @Override
    public String parseUserId(String token) {
        return token;
    }

    @Override
    public Collection<? extends GrantedAuthority> parseGrantedAuthorities(String token) {
        return null;
    }
}
