package com.atoz.authentication;

import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface AuthMapper {
    Optional<JwtSigninDTO> findById(String userId);

    void saveRefreshToken(RefreshToken refreshToken);

    void updateRefreshToken(RefreshToken refreshToken);

    Optional<RefreshToken> findByKey(String tokenKey);

    void deleteRefreshToken(String tokenKey);
}
