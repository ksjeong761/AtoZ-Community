package com.atoz.security.authentication;

import com.atoz.security.authentication.dto.request.SignoutRequestDto;
import com.atoz.security.authentication.dto.request.TokenRequestDto;
import com.atoz.security.authentication.dto.response.TokenResponseDto;
import com.atoz.security.authentication.dto.request.SigninRequestDto;

public interface AuthenticationService {

    TokenResponseDto signin(SigninRequestDto signinRequestDto);

    void signout(SignoutRequestDto signoutRequestDto);

    TokenResponseDto refresh(TokenRequestDto tokenRequestDto);
}
