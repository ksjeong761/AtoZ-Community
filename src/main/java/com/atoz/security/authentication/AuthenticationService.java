package com.atoz.security.authentication;

import com.atoz.security.authentication.dto.TokenRequestDTO;
import com.atoz.security.authentication.dto.TokenResponseDTO;
import com.atoz.security.authentication.dto.SigninDTO;

public interface AuthenticationService {
    TokenResponseDTO signin(SigninDTO signinDTO);

    void signout(TokenRequestDTO tokenRequestDTO);

    TokenResponseDTO refresh(TokenRequestDTO tokenRequestDTO);
}
