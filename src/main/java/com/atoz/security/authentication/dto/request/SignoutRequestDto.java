package com.atoz.security.authentication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
public class SignoutRequestDto {

    @NotNull(message = "아이디를 반드시 입력해주세요")
    String userId;
}
