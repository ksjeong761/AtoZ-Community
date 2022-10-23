package com.atoz.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "/application-test.properties")
@MybatisTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("회원가입 SQL Mapper 테스트")
    void addUser() {
        User user = new User();
        user.setUserId("userId");
        user.setEmail("email");
        user.setNickname("nickname");
        user.setPassword("password");

        userMapper.addUser(user);
    }

}