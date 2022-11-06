package com.atoz.login;

import com.atoz.user.UserMapper;
import com.atoz.user.UserRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@TestPropertySource(locations = "/application-test.yaml")
@MybatisTest
public class LoginInfoMapperTest {

    private UserRequestDTO expectedUserRequestDTO;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginMapper loginMapper;

    @BeforeEach
    public void beforeEach() {
        expectedUserRequestDTO = new UserRequestDTO("testId", "testPassword", "testNickname", "test@test.com");
    }

    @Test
    void 유저아이디로_유저_정보_조회_성공() {
        userMapper.addUser(expectedUserRequestDTO);

        LoginDTO actualLoginDTO = loginMapper.findById(expectedUserRequestDTO.getUserId());
        log.info("findUser id={}, password={}", actualLoginDTO.getUserId(), actualLoginDTO.getPassword());

        isEquality(expectedUserRequestDTO, actualLoginDTO);
    }

    @Test
    void 유저아이디로_유저_정보_조회_실패() {
        userMapper.addUser(expectedUserRequestDTO);

        String findUserName = "test";
        LoginDTO findUser = loginMapper.findById("test");

        assertThat(findUser).isNull();
    }

    private void isEquality(UserRequestDTO actual, LoginDTO expected) {
        assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
    }
}
