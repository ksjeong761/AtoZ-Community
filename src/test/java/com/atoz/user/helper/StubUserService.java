package com.atoz.user.helper;

import com.atoz.user.UserService;
import com.atoz.user.dto.UserResponseDTO;
import com.atoz.user.entity.UserEntity;

public class StubUserService implements UserService {

    @Override
    public UserResponseDTO signup(UserEntity userEntity) {
        return null;
    }
}
