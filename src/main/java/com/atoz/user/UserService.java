package com.atoz.user;

import com.atoz.user.dto.ChangePasswordDTO;
import com.atoz.user.dto.UserResponseDTO;
import com.atoz.user.dto.UserUpdateDTO;
import com.atoz.user.entity.UserEntity;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    UserResponseDTO signup(UserEntity userEntity);

    void update(UserUpdateDTO userUpdateDTO);

    void changePassword(ChangePasswordDTO changePasswordDTO);

    void delete(String userId);
}
