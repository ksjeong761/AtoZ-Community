package com.atoz.login;

import com.atoz.ApiResponse;
import com.atoz.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    MockMvc mockMvc;

    @InjectMocks
    LoginController loginController;

    @Mock
    LoginService loginService;

    ObjectMapper objectMapper;

    LoginDTO testLoginDTO;

    LoginDTO encryptedTestLoginInfo;

    @BeforeEach
    public void beforeEach() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(loginController)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .setControllerAdvice(ExceptionControllerAdvice.class)
                .build();

        objectMapper = new ObjectMapper();

        testLoginDTO = LoginDTO.builder()
                .userId("testId")
                .password("testPassword")
                .build();

        encryptedTestLoginInfo = LoginDTO.builder()
                .userId("testId")
                .password("testPassword")
                .build();
    }

    @Test
    void 올바른_아이디_비밀번호_쌍_로그인_성공() throws Exception {
        given(loginService.getLoginInfo(any(LoginDTO.class))).willReturn(encryptedTestLoginInfo);

        ResultActions actions = mockMvc.perform(post("/user/login")
                .content(objectMapper.writeValueAsString(testLoginDTO))
                .contentType(MediaType.APPLICATION_JSON));

        ApiResponse<LoginDTO> data = ApiResponse.<LoginDTO>builder()
                .message("login success")
                .data(LoginDTO.builder().userId("testId").build()).build();
        actions.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(data)));
        verify(loginService).getLoginInfo(any(LoginDTO.class));
    }

    @Test
    void 틀린_아이디_비밀번호_쌍_로그인_실패() throws Exception {
        LoginDTO otherLoginDTO = LoginDTO.builder()
                .userId("testId")
                .password("testPassword2")
                .build();
        given(loginService.getLoginInfo(any(LoginDTO.class))).willThrow(new LoginValidationException("패스워드 값이 일치하지 않습니다."));

        ResultActions actions = mockMvc
                .perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(otherLoginDTO)))
                .andDo(print());

        ErrorResponse errorResponse = new ErrorResponse("패스워드 값이 일치하지 않습니다.");

        actions.andExpect(status().isUnauthorized())
                .andExpect(content().string(objectMapper.writeValueAsString(errorResponse)));
        verify(loginService).getLoginInfo(any(LoginDTO.class));
    }
}
