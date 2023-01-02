package com.atoz.user;

import com.atoz.user.dto.request.ChangePasswordRequestDto;
import com.atoz.user.dto.request.UpdateUserRequestDto;
import com.atoz.user.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {

    void addUser(UserDto userDto);

    void addAuthority(UserDto userDto);

    Optional<UserDto> findUserByUserId(String userId);

    void updateUser(@Param("updateUserRequestDto") UpdateUserRequestDto updateUserRequestDto,
                    @Param("userId") String userId);

    void changePassword(@Param("changePasswordRequestDto") ChangePasswordRequestDto changePasswordRequestDto,
                        @Param("userId") String userId);

    void deleteUser(String userId);
}
