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

    LoginRequestDTO testLoginRequestDTO;

    LoginRequestDTO encryptedTestLoginRequestDTO;

    @BeforeEach
    public void beforeEach() {
        testLoginRequestDTO = LoginRequestDTO.builder()
                .userId("testId")
                .password("testPassword")
                .build();

        encryptedTestLoginRequestDTO = LoginRequestDTO.builder()
                .userId("testId")
                .password("testPassword")
                .build();
    }

    @Test
    void 올바른_아이디_비밀번호_쌍_로그인_성공() {
        when(loginMapper.findById(testLoginRequestDTO.getUserId())).thenReturn(encryptedTestLoginRequestDTO);

        LoginRequestDTO loginRequestDTO = loginServiceImpl.getLoginInfo(testLoginRequestDTO);

        verify(loginMapper).findById("testId");
        assertThat(loginRequestDTO).isEqualTo(encryptedTestLoginRequestDTO);
    }

    @Test
    void 틀린_아이디_비밀번호_쌍_로그인_실패() {
        LoginRequestDTO otherLoginRequestDTO = LoginRequestDTO.builder()
                .userId("testId")
                .password("testPassword2")
                .build();

        when(loginMapper.findById(otherLoginRequestDTO.getUserId())).thenReturn(encryptedTestLoginRequestDTO);

        Assertions.assertThatThrownBy(() -> loginServiceImpl.getLoginInfo(otherLoginRequestDTO))
                .isInstanceOf(LoginValidationException.class);
        verify(loginMapper).findById("testId");
    }
}