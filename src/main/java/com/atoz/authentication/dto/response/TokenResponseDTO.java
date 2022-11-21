package com.atoz.authentication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDTO {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
