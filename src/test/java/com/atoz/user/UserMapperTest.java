package com.atoz.user;

import com.atoz.login.LoginMapper;
import com.atoz.login.LoginDTO;
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
    void addUser_회원가입에_성공해야한다() {
        UserRequestDTO userRequestDTO = new UserRequestDTO(
                "testUserId",
                "testPassword",
                "testNickname",
                "test@test.com");

        userMapper.addUser(userRequestDTO);
        LoginDTO addedUser = loginMapper.findById("testUserId");

        Assertions.assertThat(addedUser.getUserId()).isEqualTo("testUserId");
        Assertions.assertThat(addedUser.getPassword()).isEqualTo("testPassword");
    }

    @Test
    void addUser_아이디가_중복되면_회원가입에_실패해야한다() {
        UserRequestDTO userRequestDTO = new UserRequestDTO(
                "testUserId",
                "testPassword",
                "testNickname",
                "test@test.com");

        Assertions.assertThatThrownBy(() -> {
            userMapper.addUser(userRequestDTO);
            userMapper.addUser(userRequestDTO);
        }).isInstanceOf(DataAccessException.class);
    }
}