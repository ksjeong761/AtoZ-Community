package com.atoz.security.authentication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class TokenRequestDto {

    @NotNull(message = "액세스 토큰이 필요합니다.")
    private String accessToken;

    @NotNull(message = "리프레시 토큰이 필요합니다.")
    private String refreshToken;
}
