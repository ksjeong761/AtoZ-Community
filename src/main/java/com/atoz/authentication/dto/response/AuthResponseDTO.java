package com.atoz.authentication.dto.response;

import lombok.Getter;

@Getter
public class AuthResponseDTO {
    private final String message;

    public AuthResponseDTO(String message) {
        this.message = message;
    }
}
