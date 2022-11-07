package com.atoz.user;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestPropertySource(locations = "/application-test.yaml")
@MybatisTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    private SignupDTO signedUpUser;

    @BeforeEach
    public void beforeEach() {
        signedUpUser = new SignupDTO("testId", "testPassword", "testNickname", "test@test.com");
        userMapper.addUser(new UserEntity(signedUpUser));
    }

    @Test
    void addUser_회원가입에_성공해야한다() {
        SignupDTO signupDTO = new SignupDTO("testUserId", "testPassword", "testNickname", "test@test.com");

        userMapper.addUser(new UserEntity(signupDTO));
        UserEntity addedUser = userMapper.findById("testUserId");

        Assertions.assertThat(addedUser.getUserId()).isEqualTo("testUserId");
    }

    @Test
    void addUser_이미_가입되어있다면_회원가입에_실패해야한다() {
        SignupDTO signupDTO = new SignupDTO("testUserId","testPassword","testNickname","test@test.com");

        Assertions.assertThatThrownBy(() -> {
            userMapper.addUser(new UserEntity(signupDTO));
            userMapper.addUser(new UserEntity(signupDTO));
        }).isInstanceOf(DataAccessException.class);
    }

    @Test
    void findById_사용자정보를_조회할수있다() {
        UserEntity foundUser = userMapper.findById(signedUpUser.getUserId());
        log.info("foundUser id={}, password={}", foundUser.getUserId(), foundUser.getPassword());

        assertThat(signedUpUser.getUserId()).isEqualTo(foundUser.getUserId());
    }

    @Test
    void findById_가입되지않은_사용자정보를_조회할수없다() {
        UserEntity findUser = userMapper.findById("wrong userId");

        assertThat(findUser).isNull();
    }
}