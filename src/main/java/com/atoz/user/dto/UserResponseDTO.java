package com.atoz.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResponseDTO {
    private final String userId;
    private final String nickname;
    private final String email;
}
