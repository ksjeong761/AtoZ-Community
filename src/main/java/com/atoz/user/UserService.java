package com.atoz.user;

public interface UserService {

    UserResponseDTO signup(SignupDTO signupDTO);

    UserResponseDTO signin(SigninDTO signinDTO);

}
