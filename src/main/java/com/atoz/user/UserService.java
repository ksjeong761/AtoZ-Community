package com.atoz.user;

public interface UserService {

    UserResponseDTO register(RegisterDTO registerDTO);

    LoginDTO getLoginInfo(LoginDTO loginDTO);

}
