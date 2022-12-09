package com.atoz.user;

import com.atoz.user.dto.ChangePasswordDTO;
import com.atoz.user.dto.UpdateUserDTO;
import com.atoz.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {

    void addUser(UserEntity userEntity);

    void addAuthority(UserEntity userEntity);

    Optional<UserEntity> findById(String userId);

    void updateUser(UpdateUserDTO updateUserDTO);

    void changePassword(ChangePasswordDTO changePasswordDTO);

    void deleteUser(String userId);
}
