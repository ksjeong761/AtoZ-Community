package com.atoz.authentication.help;

import com.atoz.security.authentication.dto.TokenRequestDTO;
import com.atoz.security.authentication.dto.TokenResponseDTO;
import com.atoz.security.authentication.AuthenticationService;
import com.atoz.security.authentication.dto.SigninDTO;

public class DummyAuthenticationService implements AuthenticationService {

    @Override
    public TokenResponseDTO signin(SigninDTO signinDTO) {
        return null;
    }

    @Override
    public void signout(TokenRequestDTO tokenRequestDTO) { }

    @Override
    public TokenResponseDTO refresh(TokenRequestDTO tokenRequestDTO) {
        return null;
    }
}