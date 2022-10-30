package com.atoz.login;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

    @Mock
    LoginMapper loginMapper;

    @InjectMocks
    LoginServiceImpl loginServiceImpl;

    LoginInfo testLoginInfo;

    LoginInfo encryptedTestLoginInfo;

    @BeforeEach
    public void beforeEach() {
        testLoginInfo = LoginInfo.builder()
                .userId("testId")
                .password("testPassword")
                .build();

        encryptedTestLoginInfo = LoginInfo.builder()
                .userId("testId")
                .password("testPassword")
                .build();
    }

    @Test
    void 올바른_아이디_비밀번호_쌍_로그인_성공() {
        when(loginMapper.findById(testLoginInfo.getUserId())).thenReturn(encryptedTestLoginInfo);

        LoginInfo loginInfo = loginServiceImpl.getLoginInfo(testLoginInfo);

        verify(loginMapper).findById("testId");
        assertThat(loginInfo).isEqualTo(encryptedTestLoginInfo);
    }

    @Test
    void 틀린_아이디_비밀번호_쌍_로그인_실패() {
        LoginInfo otherLoginInfo = LoginInfo.builder()
                .userId("testId")
                .password("testPassword2")
                .build();

        when(loginMapper.findById(otherLoginInfo.getUserId())).thenReturn(encryptedTestLoginInfo);

        Assertions.assertThatThrownBy(() -> loginServiceImpl.getLoginInfo(otherLoginInfo))
                .isInstanceOf(LoginValidationException.class);
        verify(loginMapper).findById("testId");
    }
}