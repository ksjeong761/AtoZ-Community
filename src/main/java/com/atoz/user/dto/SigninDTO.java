package com.atoz.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SigninDTO {

    @NotNull(message = "아이디를 반드시 입력해주세요")
    private String userId;

    @NotNull(message = "패스워드를 반드시 입력해주세요")
    private String password;

    public Authentication toAuthentication() {
        return new UsernamePasswordAuthenticationToken(getUserId(), getPassword());
    }
}
