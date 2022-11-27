package com.atoz.security.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenRequestDTO {
    private String accessToken;
    private String refreshToken;
}
