package com.atoz.user.helper;

import com.atoz.user.UserService;
import com.atoz.user.dto.ChangePasswordDTO;
import com.atoz.user.dto.UserResponseDTO;
import com.atoz.user.dto.UpdateUserDTO;
import com.atoz.user.entity.UserEntity;

public class SpyUserService implements UserService {

    public String encodedPassword;

    @Override
    public UserResponseDTO signup(UserEntity userEntity) {
        encodedPassword = userEntity.getPassword();

        return null;
    }

    @Override
    public void update(UpdateUserDTO updateUserDTO) {

    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        encodedPassword = changePasswordDTO.getPassword();
    }

    @Override
    public void delete(String userId) {

    }
}
