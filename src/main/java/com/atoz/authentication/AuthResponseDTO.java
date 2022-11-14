package com.atoz.authentication;

import lombok.Getter;

@Getter
public class AuthResponseDTO {

    private final String message;

    public AuthResponseDTO(String message) {
        this.message = message;
    }

}
