package com.atoz.login;

import com.atoz.user.User;
import com.atoz.user.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@TestPropertySource(locations = "/application-test.properties")
@MybatisTest
public class LoginInfoMapperTest {

    private User user;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginMapper loginMapper;

    @BeforeEach
    public void beforeEach() {
        user = new User();
        user.setUserId("testId");
        user.setPassword("testPassword");
        user.setNickname("testNickname");
        user.setEmail("test@test.com");
    }

    @Test
    void 유저아이디로_유저_정보_조회_성공() {
        userMapper.addUser(user);

        LoginRequestDTO findUser = loginMapper.findById(user.getUserId());
        log.info("findUser id={}, password={}", findUser.getUserId(), findUser.getPassword());

        saveAndFindUserEquality(user, findUser);
    }

    @Test
    void 유저아이디로_유저_정보_조회_실패() {
        userMapper.addUser(user);

        String findUserName = "test";
        LoginRequestDTO findUser = loginMapper.findById("test");

        assertThat(findUser).isNull();
    }

    private void saveAndFindUserEquality(User user, LoginRequestDTO findUser) {
        assertThat(findUser.getUserId()).isEqualTo(user.getUserId());
        assertThat(findUser.getPassword()).isEqualTo(user.getPassword());
    }
}
