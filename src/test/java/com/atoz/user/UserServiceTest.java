package com.atoz.user;

import com.atoz.error.LoginValidationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class UserServiceTest {

    UserService loginService;

    SpyStubLoginMapper loginMapper;

    @BeforeEach
    public void beforeEach() {
        loginMapper = new SpyStubLoginMapper();
        loginService = new UserServiceImpl(loginMapper);
    }

    @Test
    void 올바른_아이디_비밀번호_쌍_로그인_성공() {
        LoginDTO expectedLoginDTO = LoginDTO.builder()
                .userId("testId")
                .password("testPassword")
                .build();

        LoginDTO actualLoginDTO = loginService.getLoginInfo(expectedLoginDTO);

        assertThat(loginMapper.getCallFindByIdCount()).isEqualTo(1);
        isEquality(actualLoginDTO, expectedLoginDTO);
    }

    @Test
    void 미등록_유저_아이디_로그인_실패() {
        LoginDTO expectedLoginDTO = LoginDTO.builder()
                .userId("testId2")
                .password("testPassword")
                .build();


        assertThatThrownBy(() ->
                loginService.getLoginInfo(expectedLoginDTO))
                .isInstanceOf(LoginValidationException.class)
                .hasMessage("해당 유저가 존재하지 않습니다.");
        assertThat(loginMapper.getCallFindByIdCount()).isEqualTo(1);
    }

    @Test
    void 틀린_아이디_로그인_쌍_실패() {
        LoginDTO expectedLoginDTO = LoginDTO.builder()
                .userId("testId")
                .password("testPassword2")
                .build();

        assertThatThrownBy(() ->
                loginService.getLoginInfo(expectedLoginDTO))
                .isInstanceOf(LoginValidationException.class)
                .hasMessage("패스워드 값이 일치하지 않습니다.");
        assertThat(loginMapper.getCallFindByIdCount()).isEqualTo(1);
    }

    private void isEquality(LoginDTO actual, LoginDTO expected) {
        assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
    }

    static class SpyStubLoginMapper implements UserMapper {

        private int callFindByIdCount = 0;

        @Override
        public void addUser(RegisterDTO registerDTO) {

        }

        @Override
        public LoginDTO findById(String userId) {
            this.callFindByIdCount++;

            if (userId.equals("testId")) {
                return LoginDTO.builder()
                        .userId("testId")
                        .password("testPassword")
                        .build();
            } else {
                return null;
            }

        }

        public int getCallFindByIdCount() {
            return callFindByIdCount;
        }
    }
}