package com.atoz.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDTO {

    private final String userId;

    private final String nickname;

    private final String email;

    public UserResponseDTO(SignupDTO signupDTO) {
        this.userId = signupDTO.getUserId();
        this.nickname = signupDTO.getNickname();
        this.email = signupDTO.getEmail();
    }
}
