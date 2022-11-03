package com.atoz.login;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;

@Slf4j
class LoginServiceImplTest {

    LoginServiceImpl loginServiceImpl;

    SpyStubLoginMapper loginMapper;

    @BeforeEach
    public void beforeEach() {
        loginMapper = new SpyStubLoginMapper();
        loginServiceImpl = new LoginServiceImpl(loginMapper);
    }

    @Test
    void 올바른_아이디_비밀번호_쌍_로그인_성공() {
        LoginRequestDTO expectedLoginRequestDTO = LoginRequestDTO.builder()
                .userId("testId")
                .password("testPassword")
                .build();

        LoginRequestDTO actualLoginRequestDTO = loginServiceImpl.getLoginInfo(expectedLoginRequestDTO);

        assertThat(loginMapper.getCallFindByIdCount()).isEqualTo(1);
        isEquality(actualLoginRequestDTO, expectedLoginRequestDTO);
    }

    @Test
    void 미등록_유저_아이디_로그인_실패() {
        LoginRequestDTO expectedLoginRequestDTO = LoginRequestDTO.builder()
                .userId("testId2")
                .password("testPassword")
                .build();


        assertThatThrownBy(() ->
                loginServiceImpl.getLoginInfo(expectedLoginRequestDTO))
                .isInstanceOf(LoginValidationException.class)
                .hasMessage("해당 유저가 존재하지 않습니다.");
        assertThat(loginMapper.getCallFindByIdCount()).isEqualTo(1);
    }

    @Test
    void 틀린_아이디_로그인_쌍_실패() {
        LoginRequestDTO expectedLoginRequestDTO = LoginRequestDTO.builder()
                .userId("testId")
                .password("testPassword2")
                .build();

        assertThatThrownBy(() ->
                loginServiceImpl.getLoginInfo(expectedLoginRequestDTO))
                .isInstanceOf(LoginValidationException.class)
                .hasMessage("패스워드 값이 일치하지 않습니다.");
        assertThat(loginMapper.getCallFindByIdCount()).isEqualTo(1);
    }

    private void isEquality(LoginRequestDTO actual, LoginRequestDTO expected) {
        assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
    }

    static class SpyStubLoginMapper implements LoginMapper {

        private int callFindByIdCount = 0;

        @Override
        public LoginRequestDTO findById(String userId) {
            this.callFindByIdCount++;

            if (userId.equals("testId")) {
                return LoginRequestDTO.builder()
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