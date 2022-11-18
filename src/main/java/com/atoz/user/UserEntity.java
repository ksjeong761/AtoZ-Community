package com.atoz.user;

import com.atoz.authentication.Authority;
import com.atoz.authentication.MemberAuth;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
public class UserEntity {

    private String userId;

    private String password;

    private String nickname;

    private String email;

    private Authority authority;

    public UserEntity(String userId, String password, String nickname, String email, Authority authority) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.authority = authority;
    }

    public UserEntity(SignupDTO signupDTO) {
        this.userId = signupDTO.getUserId();
        this.password = signupDTO.getPassword();
        this.email = signupDTO.getEmail();
        this.nickname = signupDTO.getNickname();
        this.authority = Authority.builder().authorityName(MemberAuth.ROLE_USER).build();
    }

    public UserEntity(PasswordEncoder passwordEncoder, SignupDTO signupDTO) {

        String password = passwordEncoder.encode(signupDTO.getPassword());

        this.userId = signupDTO.getUserId();
        this.password = password;
        this.email = signupDTO.getEmail();
        this.nickname = signupDTO.getNickname();
        this.authority = Authority.builder().authorityName(MemberAuth.ROLE_USER).build();
    }
}
