package com.atoz.authentication.testDouble;

import com.atoz.authentication.RefreshTokenMapper;
import com.atoz.authentication.JwtSigninDTO;
import com.atoz.authentication.RefreshToken;

import java.util.*;

public class TestRefreshTokenMapper implements RefreshTokenMapper {
    private static Map<String, JwtSigninDTO> users = new HashMap<>();
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
