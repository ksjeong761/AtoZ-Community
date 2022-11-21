package com.atoz.authentication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenRequestDTO {
    private String accessToken;
    private String refreshToken;
}
