package com.atoz.user.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class UserDeleteDTO {

    @NotNull(message = "아이디를 반드시 입력해주세요.")
    @Size(min = 1, max = 20, message = "1 ~ 20자 사이의 아이디를 입력해주세요.")
    String userId;
}
