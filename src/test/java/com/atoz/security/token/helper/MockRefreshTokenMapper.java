package com.atoz.security.token.helper;

import com.atoz.security.token.RefreshTokenMapper;
import com.atoz.security.token.dto.RefreshTokenDto;

import java.util.*;

public class MockRefreshTokenMapper implements RefreshTokenMapper {
    private static final Map<String, RefreshTokenDto> tokens = new HashMap<>();

    @Override
    public void saveToken(RefreshTokenDto refreshTokenDto) {
        tokens.put(refreshTokenDto.getTokenKey(), refreshTokenDto);
    }

    @Override
    public void updateToken(RefreshTokenDto refreshTokenDto) {
        tokens.put(refreshTokenDto.getTokenKey(), refreshTokenDto);
    }

    @Override
    public Optional<RefreshTokenDto> findTokenByKey(String tokenKey) {
        return Optional.ofNullable(tokens.get(tokenKey));
    }

    @Override
    public void deleteToken(String tokenKey) {
        tokens.remove(tokenKey);
    }
}
