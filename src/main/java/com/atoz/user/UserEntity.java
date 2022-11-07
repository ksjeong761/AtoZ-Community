package com.atoz.user;

import com.atoz.cryptography.HashManager;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserEntity {

    private String userId;

    private byte[] password;

    private String nickname;

    private String email;

    private byte[] passwordSalt;

    public UserEntity(SignupDTO signupDTO) {
        HashManager hashManager = new HashManager();
        byte[] salt = hashManager.makeSalt();
        byte[] hashedPassword = hashManager.hashString(signupDTO.getPassword(), salt);

        this.userId = signupDTO.getUserId();
        this.password = hashedPassword;
        this.email = signupDTO.getEmail();
        this.nickname = signupDTO.getNickname();
        this.passwordSalt = salt;
    }
}
