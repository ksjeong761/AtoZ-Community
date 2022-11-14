package com.atoz.authentication.testDouble;

import com.atoz.authentication.AuthMapper;
import com.atoz.authentication.JwtSigninDTO;
import com.atoz.authentication.RefreshToken;

import java.util.*;

public class TestAuthMapper implements AuthMapper {
    private static Map<String, JwtSigninDTO> users = new HashMap<>();
    private static Map<String, RefreshToken> tokens = new HashMap<>();

    @Override
    public Optional<JwtSigninDTO> findById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public void saveRefreshToken(RefreshToken refreshToken) {
        tokens.put(refreshToken.getTokenKey(), refreshToken);
    }

    @Override
    public void updateRefreshToken(RefreshToken refreshToken) {
        tokens.put(refreshToken.getTokenKey(), refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByKey(String tokenKey) {
        return Optional.ofNullable(tokens.get(tokenKey));
    }

    @Override
    public void deleteRefreshToken(String tokenKey) {
        tokens.remove(tokenKey);
    }
}
