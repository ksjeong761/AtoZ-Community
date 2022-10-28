package com.atoz.user;

import com.atoz.login.entity.LoginInfo;
import com.atoz.login.mapper.LoginMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "/application-test.yaml")
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
        LoginInfo addedUser = loginMapper.findById("testUserId");

        Assertions.assertThat(addedUser.getUserId()).isEqualTo("testUserId");
        Assertions.assertThat(addedUser.getPassword()).isEqualTo("testPassword");
    }

    @Test
    void 회원_아이디가_중복되면_저장_실패() {
        User user = new User();
        user.setUserId("testUserId");
        user.setEmail("testEmail");
        user.setNickname("testNickname");
        user.setPassword("testPassword");

        Assertions.assertThatThrownBy(() -> {
            userMapper.addUser(user);
            userMapper.addUser(user);
        }).isInstanceOf(DataAccessException.class);
    }
}