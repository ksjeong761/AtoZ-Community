package com.atoz.authentication.service;

import com.atoz.authentication.dto.request.TokenRequestDTO;
import com.atoz.authentication.dto.response.TokenResponseDTO;
import com.atoz.user.SigninDTO;

public interface AuthenticationService {
    TokenResponseDTO signin(SigninDTO signinDTO);

    void signout(TokenRequestDTO tokenRequestDTO);

    TokenResponseDTO refresh(TokenRequestDTO tokenRequestDTO);
}
