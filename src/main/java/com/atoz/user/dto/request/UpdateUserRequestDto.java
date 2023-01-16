package com.atoz.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDto {

    @NotNull(message = "아이디를 반드시 입력해주세요.")
    @Size(min = 1, max = 20, message = "1 ~ 20자 사이의 아이디를 입력해주세요.")
    private String userId;

    @NotNull(message = "닉네임을 반드시 입력해주세요.")
    @Size(min = 1, max = 20, message = "1 ~ 20자 사이의 닉네임을 입력해주세요.")
    private String nickname;

    @NotBlank(message = "이메일을 반드시 입력해주세요.")
    @Email(message = "이메일 형식이 잘못되었습니다.")
    private String email;

    @NotNull(message = "나이를 반드시 입력해주세요.")
    @Min(value = 1, message = "1 이상의 나이를 입력해주세요.")
    @Max(value = 200, message = "200 이하의 나이를 입력해주세요.")
    private int age;
}
