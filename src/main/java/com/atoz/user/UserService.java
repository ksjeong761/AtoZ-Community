package com.atoz.user;

import com.atoz.user.dto.ChangePasswordDTO;
import com.atoz.user.dto.UserResponseDTO;
import com.atoz.user.dto.UpdateUserDTO;
import com.atoz.user.entity.UserEntity;

public interface UserService {

    UserResponseDTO signup(UserEntity userEntity);

    void update(UpdateUserDTO updateUserDTO);

    void changePassword(ChangePasswordDTO changePasswordDTO);

    void delete(String userId);
}
