package com.atoz.user;

import lombok.Getter;

@Getter
public class UserResponseDTO {

    private final String userId;

    private final String nickname;

    private final String email;

    public UserResponseDTO(RegisterDTO registerDTO) {
        this.userId = registerDTO.getUserId();
        this.nickname = registerDTO.getNickname();
        this.email = registerDTO.getEmail();
    }
}
