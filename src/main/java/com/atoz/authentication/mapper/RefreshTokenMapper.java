package com.atoz.authentication.mapper;

import com.atoz.authentication.entity.RefreshTokenEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface RefreshTokenMapper {
    void saveToken(RefreshTokenEntity refreshTokenEntity);

    void updateToken(RefreshTokenEntity refreshTokenEntity);

    Optional<RefreshTokenEntity> findTokenByKey(String tokenKey);

    void deleteToken(String tokenKey);
}
