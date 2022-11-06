package com.atoz.user;

public interface UserService {

    UserResponseDTO signup(SignupDTO signupDTO);

    SigninDTO findSigninInfo(SigninDTO signinDTO);

}
