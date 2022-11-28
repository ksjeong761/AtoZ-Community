package com.atoz.security.authentication.help;

import com.atoz.security.token.TokenProvider;
import com.atoz.user.entity.Authority;

import java.util.Set;

public class StubTokenProvider implements TokenProvider {
    @Override
    public String createAccessToken(String userId, Set<Authority> authorities) {
        return userId;
    }

    @Override
    public String createRefreshToken(String userId, Set<Authority> authorities) {
        return userId;
    }
}
