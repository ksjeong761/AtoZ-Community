package com.atoz.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    LoginService loginService;

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
    void 올바른_아이디_비밀번호_쌍_로그인_성공() throws Exception {
        //given
        given(loginService.getLoginInfo(any(LoginInfo.class))).willReturn(encryptedTestLoginInfo);

        //when
        ResultActions actions = mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userId", testLoginInfo.getUserId())
                .param("password", testLoginInfo.getPassword()));


        //then
        actions.andExpect(status().isOk())
                .andExpect(content().string("login success"));

        verify(loginService).getLoginInfo(any(LoginInfo.class));
    }

    @Test
    void 틀린_아이디_비밀번호_쌍_로그인_실패() throws Exception {
        //given
        LoginInfo otherLoginInfo = LoginInfo.builder()
                .userId("testId")
                .password("testPassword2")
                .build();

        given(loginService.getLoginInfo(any(LoginInfo.class))).willReturn(null);

        //when
        ResultActions actions = mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userId", otherLoginInfo.getUserId())
                .param("password", otherLoginInfo.getPassword()))
                .andDo(print());


        //then
        actions.andExpect(status().isOk())
                .andExpect(content().string("fail"));

        verify(loginService).getLoginInfo(any(LoginInfo.class));
    }
}
