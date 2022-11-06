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

    private SignupDTO expectedSignupDTO;

    @BeforeEach
    public void beforeEach() {
        expectedSignupDTO = new SignupDTO("testId", "testPassword", "testNickname", "test@test.com");
    }

    @Test
    void addUser_회원가입에_성공해야한다() {
        SignupDTO signupDTO = new SignupDTO(
                "testUserId",
                "testPassword",
                "testNickname",
                "test@test.com");

        userMapper.addUser(signupDTO);
        SigninDTO addedUser = userMapper.findById("testUserId");

        Assertions.assertThat(addedUser.getUserId()).isEqualTo("testUserId");
        Assertions.assertThat(addedUser.getPassword()).isEqualTo("testPassword");
    }

    @Test
    void addUser_아이디가_중복되면_회원가입에_실패해야한다() {
        SignupDTO signupDTO = new SignupDTO(
                "testUserId",
                "testPassword",
                "testNickname",
                "test@test.com");

        Assertions.assertThatThrownBy(() -> {
            userMapper.addUser(signupDTO);
            userMapper.addUser(signupDTO);
        }).isInstanceOf(DataAccessException.class);
    }


    @Test
    void 유저아이디로_유저_정보_조회_성공() {
        userMapper.addUser(expectedSignupDTO);

        SigninDTO actualSigninDTO = userMapper.findById(expectedSignupDTO.getUserId());
        log.info("findUser id={}, password={}", actualSigninDTO.getUserId(), actualSigninDTO.getPassword());

        isEquality(expectedSignupDTO, actualSigninDTO);
    }

    @Test
    void 유저아이디로_유저_정보_조회_실패() {
        userMapper.addUser(expectedSignupDTO);

        String findUserName = "test";
        SigninDTO findUser = userMapper.findById("test");

        assertThat(findUser).isNull();
    }

    private void isEquality(SignupDTO actual, SigninDTO expected) {
        assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
    }
}