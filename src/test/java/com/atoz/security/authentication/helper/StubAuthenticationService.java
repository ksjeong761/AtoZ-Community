package com.atoz.security.authentication.helper;

import com.atoz.security.authentication.dto.request.SignoutRequestDto;
import com.atoz.security.authentication.dto.request.TokenRequestDto;
import com.atoz.security.authentication.dto.response.TokenResponseDto;
import com.atoz.security.authentication.AuthenticationService;
import com.atoz.security.authentication.dto.request.SigninRequestDto;

public class StubAuthenticationService implements AuthenticationService {

    @Override
    public TokenResponseDto signin(SigninRequestDto signinRequestDto) {
        return null;
    }

    @Override
    public void signout(SignoutRequestDto signoutRequestDto) { }

    @Override
    public TokenResponseDto refresh(TokenRequestDto tokenRequestDto) {
        return null;
    }
}