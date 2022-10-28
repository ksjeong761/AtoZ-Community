package com.atoz.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class User {

    @NotBlank(message = "아이디를 입력해야 합니다.")
    @Size(min = 1, max = 20, message = "아이디는 1 ~ 20자 이여야 합니다.")
    String userId;

    @NotBlank(message = "패스워드를 입력해야 합니다.")
    @Size(min = 1, max = 20, message = "패스워드는 1 ~ 20자 이여야 합니다.")
    String password;

    @NotBlank(message = "닉네임을 입력해야 합니다.")
    @Size(min = 1, max = 20, message = "닉네임은 1 ~ 20자 이여야 합니다.")
    String nickname;

    @NotBlank(message = "이메일을 입력해야 합니다.")
    @Email(message = "이메일 형식이 잘못되었습니다.")
    String email;
}
