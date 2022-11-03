package com.atoz.user;

import lombok.Getter;

@Getter
public class UserResponseDTO {

    private final String userId;

    private final String nickname;

    private final String email;

    public UserResponseDTO(UserRequestDTO requestDTO) {
        this.userId = requestDTO.getUserId();
        this.nickname = requestDTO.getNickname();
        this.email = requestDTO.getEmail();
    }
}
