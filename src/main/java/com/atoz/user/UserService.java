package com.atoz.user;

import com.atoz.user.dto.SignupDTO;
import com.atoz.user.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO signup(SignupDTO signupDTO);
}
