package com.atoz.authentication.help;

import com.atoz.authentication.mapper.RefreshTokenMapper;
import com.atoz.authentication.entity.RefreshToken;

import java.util.*;

public class StubRefreshTokenMapper implements RefreshTokenMapper {
    private static Map<String, RefreshToken> tokens = new HashMap<>();

    @Override
    public void saveToken(RefreshToken refreshToken) {
        tokens.put(refreshToken.getTokenKey(), refreshToken);
    }

    @Override
    public void updateToken(RefreshToken refreshToken) {
        tokens.put(refreshToken.getTokenKey(), refreshToken);
    }

    @Override
    public Optional<RefreshToken> findTokenByKey(String tokenKey) {
        return Optional.ofNullable(tokens.get(tokenKey));
    }

    @Override
    public void deleteToken(String tokenKey) {
        tokens.remove(tokenKey);
    }
}
