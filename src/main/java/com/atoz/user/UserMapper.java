package com.atoz.user;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void addUser(SignupDTO signupDTO);

    void addUser(UserEntity userEntity);

    void addAuthority(UserEntity userEntity);

    SigninDTO findById(String userId);

}
