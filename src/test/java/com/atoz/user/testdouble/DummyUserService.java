package com.atoz.user.testdouble;

import com.atoz.user.UserRequestDTO;
import com.atoz.user.UserResponseDTO;
import com.atoz.user.UserService;

public class DummyUserService implements UserService {
    @Override
    public UserResponseDTO register(UserRequestDTO userRequestDTO) {
        return null;
    }
}
