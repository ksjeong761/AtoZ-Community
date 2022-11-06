package com.atoz.user;

import com.atoz.ApiResponse;
import com.atoz.error.LoginValidationException;
import com.atoz.error.GlobalExceptionAdvice;
import com.atoz.error.ErrorResponseDTO;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    LoginDTO testLoginDTO;

    LoginDTO encryptedTestLoginInfo;


    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .setControllerAdvice(GlobalExceptionAdvice.class)
                .build();

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
    void register_회원가입에_성공해야한다() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("userId", "12345678901234567890");
        userData.put("nickname", "testNickname");
        userData.put("password", "testPassword");
        userData.put("email", "test@test.com");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isOk());
    }

    @Test
    void register_아이디가_누락되면_회원가입에_실패해야한다() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("nickname", "testNickname");
        userData.put("password", "testPassword");
        userData.put("email", "test@test.com");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_닉네임이_누락되면_회원가입에_실패해야한다() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("userId", "12345678901234567890");
        userData.put("password", "testPassword");
        userData.put("email", "test@test.com");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_비밀번호가_누락되면_회원가입에_실패해야한다() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("userId", "12345678901234567890");
        userData.put("nickname", "testNickname");
        userData.put("email", "test@test.com");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_이메일이_누락되면_회원가입에_실패해야한다() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("userId", "12345678901234567890");
        userData.put("nickname", "testNickname");
        userData.put("password", "testPassword");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_아이디가_규칙에_맞지_않으면_회원가입에_실패해야한다() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("userId", "12345678901234567890X");
        userData.put("nickname", "testNickname");
        userData.put("password", "testPassword");
        userData.put("email", "test@test.com");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_닉네임이_규칙에_맞지_않으면_회원가입에_실패해야한다() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("userId", "12345678901234567890");
        userData.put("nickname", "");
        userData.put("password", "testPassword");
        userData.put("email", "test@test.com");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_비밀번호가_규칙에_맞지_않으면_회원가입에_실패해야한다() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("userId", "12345678901234567890");
        userData.put("nickname", "testNickname");
        userData.put("password", "");
        userData.put("email", "test@test.com");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_이메일이_규칙에_맞지_않으면_회원가입에_실패해야한다() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("userId", "12345678901234567890");
        userData.put("nickname", "testNickname");
        userData.put("password", "testPassword");
        userData.put("email", "not email");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 올바른_아이디_비밀번호_쌍_로그인_성공() throws Exception {
        given(userService.getLoginInfo(any(LoginDTO.class))).willReturn(encryptedTestLoginInfo);

        ResultActions actions = mockMvc.perform(post("/user/login")
                .content(objectMapper.writeValueAsString(testLoginDTO))
                .contentType(MediaType.APPLICATION_JSON));

        ApiResponse<LoginDTO> data = ApiResponse.<LoginDTO>builder()
                .message("login success")
                .data(LoginDTO.builder().userId("testId").build()).build();
        actions.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(data)));
        verify(userService).getLoginInfo(any(LoginDTO.class));
    }

    @Test
    void 틀린_아이디_비밀번호_쌍_로그인_실패() throws Exception {
        LoginDTO otherLoginDTO = LoginDTO.builder()
                .userId("testId")
                .password("testPassword2")
                .build();
        given(userService.getLoginInfo(any(LoginDTO.class))).willThrow(new LoginValidationException("패스워드 값이 일치하지 않습니다."));

        ResultActions actions = mockMvc
                .perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(otherLoginDTO)))
                .andDo(print());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO("패스워드 값이 일치하지 않습니다.");

        actions.andExpect(status().isUnauthorized())
                .andExpect(content().string(objectMapper.writeValueAsString(errorResponse)));
        verify(userService).getLoginInfo(any(LoginDTO.class));
    }
}