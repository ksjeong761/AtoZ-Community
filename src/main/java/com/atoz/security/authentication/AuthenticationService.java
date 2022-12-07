package com.atoz.security.authentication;

import com.atoz.security.authentication.dto.SignoutDTO;
import com.atoz.security.authentication.dto.TokenRequestDTO;
import com.atoz.security.authentication.dto.TokenResponseDTO;
import com.atoz.security.authentication.dto.SigninDTO;

public interface AuthenticationService {

    TokenResponseDTO signin(SigninDTO signinDTO);

    void signout(SignoutDTO signoutDTO);

    TokenResponseDTO refresh(TokenRequestDTO tokenRequestDTO);
}
