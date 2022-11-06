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

    private RegisterDTO expectedRegisterDTO;

    @BeforeEach
    public void beforeEach() {
        expectedRegisterDTO = new RegisterDTO("testId", "testPassword", "testNickname", "test@test.com");
    }

    @Test
    void addUser_회원가입에_성공해야한다() {
        RegisterDTO registerDTO = new RegisterDTO(
                "testUserId",
                "testPassword",
                "testNickname",
                "test@test.com");

        userMapper.addUser(registerDTO);
        LoginDTO addedUser = userMapper.findById("testUserId");

        Assertions.assertThat(addedUser.getUserId()).isEqualTo("testUserId");
        Assertions.assertThat(addedUser.getPassword()).isEqualTo("testPassword");
    }

    @Test
    void addUser_아이디가_중복되면_회원가입에_실패해야한다() {
        RegisterDTO registerDTO = new RegisterDTO(
                "testUserId",
                "testPassword",
                "testNickname",
                "test@test.com");

        Assertions.assertThatThrownBy(() -> {
            userMapper.addUser(registerDTO);
            userMapper.addUser(registerDTO);
        }).isInstanceOf(DataAccessException.class);
    }


    @Test
    void 유저아이디로_유저_정보_조회_성공() {
        userMapper.addUser(expectedRegisterDTO);

        LoginDTO actualLoginDTO = userMapper.findById(expectedRegisterDTO.getUserId());
        log.info("findUser id={}, password={}", actualLoginDTO.getUserId(), actualLoginDTO.getPassword());

        isEquality(expectedRegisterDTO, actualLoginDTO);
    }

    @Test
    void 유저아이디로_유저_정보_조회_실패() {
        userMapper.addUser(expectedRegisterDTO);

        String findUserName = "test";
        LoginDTO findUser = userMapper.findById("test");

        assertThat(findUser).isNull();
    }

    private void isEquality(RegisterDTO actual, LoginDTO expected) {
        assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
    }
}