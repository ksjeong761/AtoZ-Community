package com.atoz.user;

import com.atoz.user.help.SpyStubUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class UserServiceTest {

    SpyStubUserMapper userMapper = new SpyStubUserMapper();

    PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

    UserService userService = new UserServiceImpl(userMapper, passwordEncoder);


    @BeforeEach
    public void beforeEach() {

    }

    private void isEquality(SigninDTO actual, SigninDTO expected) {
        assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
    }
}