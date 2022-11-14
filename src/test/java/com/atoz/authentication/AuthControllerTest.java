package com.atoz.authentication;

import com.atoz.error.GlobalExceptionAdvice;
import com.atoz.user.SigninDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    AuthService authService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .setControllerAdvice(GlobalExceptionAdvice.class)
                .build();
    }

    @Test
    void signin_로그인에_성공한다() throws Exception {
        SigninDTO testSigninDTO = SigninDTO.builder()
                .userId("testId")
                .password("testPassword")
                .build();
        given(authService.signin(any(SigninDTO.class))).willReturn(TokenDTO.builder().grantType("Bearer").build());

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSigninDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void signin_아이디를_입력하지않아_로그인에_실패한다() throws Exception {
        SigninDTO testSigninDTO = SigninDTO.builder()
                .password("testPassword")
                .build();
        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSigninDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signin_패스워드를_입력하지않아_로그인에_실패한다() throws Exception {
        SigninDTO testSigninDTO = SigninDTO.builder()
                .userId("testId")
                .build();
        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSigninDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signin_아이디와패스워드를_입력하지않아_로그인에_실패한다() throws Exception {
        SigninDTO testSigninDTO = SigninDTO.builder()
                .build();
        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSigninDTO)))
                .andExpect(status().isBadRequest());
    }
}