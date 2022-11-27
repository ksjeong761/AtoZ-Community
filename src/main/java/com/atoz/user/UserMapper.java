package com.atoz.user;

import com.atoz.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {

    void addUser(UserEntity userEntity);

    void addAuthority(UserEntity userEntity);

    Optional<UserEntity> findById(String userId);
}
