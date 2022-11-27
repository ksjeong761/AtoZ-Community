package com.atoz.user;

import com.atoz.user.dto.UserResponseDTO;
import com.atoz.user.entity.UserEntity;

public interface UserService {

    UserResponseDTO signup(UserEntity userEntity);
}
