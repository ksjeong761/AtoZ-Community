package com.atoz.user;

import com.atoz.error.ControllerExceptionAdvice;
import com.atoz.user.testdouble.DummyUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private UserService userService = new DummyUserService();

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService))
                .setControllerAdvice(new ControllerExceptionAdvice())
                .build();
    }

    @Test
    void register_회원가입에_성공해야한다() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("userId", "12345678901234567890");
        userData.put("nickname", "testNickname");
        userData.put("password", "testPassword");
        userData.put("email", "test@test.com");

        mockMvc.perform(post("/user")
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

        mockMvc.perform(post("/user")
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

        mockMvc.perform(post("/user")
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

        mockMvc.perform(post("/user")
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

        mockMvc.perform(post("/user")
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

        mockMvc.perform(post("/user")
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

        mockMvc.perform(post("/user")
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

        mockMvc.perform(post("/user")
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

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isBadRequest());
    }
}