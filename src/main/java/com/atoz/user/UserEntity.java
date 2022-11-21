package com.atoz.user;

import com.atoz.authentication.entity.Authority;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Getter
@NoArgsConstructor
public class UserEntity {

    private String userId;

    private String password;

//    private byte[] password;

    private String nickname;

    private String email;

    private Set<Authority> authorities;

    @Builder
    public UserEntity(String userId, String password, String nickname, String email, Set<Authority> authorities) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.authorities = authorities;
    }

    public UserEntity(PasswordEncoder passwordEncoder, SignupDTO signupDTO, Set<Authority> authorities) {
        String password = passwordEncoder.encode(signupDTO.getPassword());

        this.userId = signupDTO.getUserId();
        this.password = password;
        this.email = signupDTO.getEmail();
        this.nickname = signupDTO.getNickname();
        this.authorities = authorities;
    }

//    private byte[] passwordSalt;

//    public UserEntity(SignupDTO signupDTO) {
//        HashManager hashManager = new HashManager();
//        byte[] salt = hashManager.makeSalt();
//        byte[] hashedPassword = hashManager.computeHash(signupDTO.getPassword(), salt);

//        this.userId = signupDTO.getUserId();
//        this.password = hashedPassword;
//        this.email = signupDTO.getEmail();
//        this.nickname = signupDTO.getNickname();
//        this.passwordSalt = salt;
//    }
}
