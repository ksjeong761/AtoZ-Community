package com.atoz.security.token;

import com.atoz.security.token.dto.RefreshTokenDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface RefreshTokenMapper {
    void saveToken(RefreshTokenDto refreshTokenDto);

    void updateToken(RefreshTokenDto refreshTokenDto);

    Optional<RefreshTokenDto> findTokenByKey(String tokenKey);

    void deleteToken(String tokenKey);
}
