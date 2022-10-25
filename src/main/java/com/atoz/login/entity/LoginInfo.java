package com.atoz.login.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
public class LoginInfo {

    @NotBlank(message = "유저 아이디가 공백일 수 없습니다.")
    private String userId;

    @NotBlank(message = "유저 패스워드가 공백일 수 없습니다.")
    private String password;

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
