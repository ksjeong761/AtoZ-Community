package com.atoz.security.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
public class SignoutDTO {

    @NotNull(message = "아이디를 반드시 입력해주세요")
    String userId;
}
