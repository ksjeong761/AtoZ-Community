package com.atoz.authentication;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtSigninDTO {
    private String userId;
    private String password;
    private String nickname;
    private String email;
    private Authority authority;

    @Builder
    public JwtSigninDTO(String userId, String password, String nickname, String email, Authority authority) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.authority = authority;
    }

}
