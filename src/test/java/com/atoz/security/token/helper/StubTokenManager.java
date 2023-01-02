package com.atoz.security.token.helper;

import com.atoz.security.token.TokenManager;
import com.atoz.user.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class StubTokenManager implements TokenManager {

    private String userId = "testUserId";

    public StubTokenManager(String userId) {
        this.userId = userId;
    }

    @Override
    public String createAccessToken(String userId, Set<Authority> authorities) {
        return "accessToken";
    }

    @Override
    public String createRefreshToken(String userId, Set<Authority> authorities) {
        return "refreshToken";
    }

    @Override
    public void validateToken(String token) {

    }

    @Override
    public String parseUserId(String token) {
        return userId;
    }
}
