package com.atoz.user.help;

import com.atoz.user.UserService;
import com.atoz.user.dto.UserResponseDTO;
import com.atoz.user.entity.UserEntity;

public class DummyUserService implements UserService {
    @Override
    public UserResponseDTO signup(UserEntity userEntity) {
        return null;
    }
}
