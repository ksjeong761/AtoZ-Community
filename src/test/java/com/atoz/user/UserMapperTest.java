package com.atoz.user;

import com.atoz.login.mapper.LoginMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "/application-test.properties")
@MybatisTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginMapper loginMapper;

    @Test
    void 회원_데이터_저장_성공() {
        User user = new User();
        user.setUserId("testUserId");
        user.setEmail("testEmail");
        user.setNickname("testNickname");
        user.setPassword("testPassword");

        userMapper.addUser(user);

        Assertions.assertThat(loginMapper.findById("testUserId").getPassword())
                .isEqualTo("testPassword");
    }
}