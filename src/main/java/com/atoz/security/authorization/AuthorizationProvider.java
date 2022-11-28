package com.atoz.security.authorization;

import org.springframework.security.core.Authentication;

public interface AuthorizationProvider {

    Authentication authorize(String jwt);
    Authentication authorize(String accessToken, String refreshToken);
}
