package com.atoz.user;

import com.atoz.error.GlobalExceptionAdvice;
import com.atoz.user.helper.DummyUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc sut;

    @BeforeEach
    public void beforeEach() {
        this.sut = MockMvcBuilders
                .standaloneSetup(new UserController(new Argon2PasswordEncoder(), new DummyUserService()))
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .setControllerAdvice(GlobalExceptionAdvice.class)
                .build();
    }

    @Test
    void signup_회원가입에_성공해야한다() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("userId", "12345678901234567890");
        userData.put("nickname", "testNickname");
        userData.put("password", "testPassword");
        userData.put("email", "test@test.com");


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)));


        resultActions.andExpect(status().isOk());
    }

    @Test
    void signup_아이디가_누락되면_회원가입에_실패해야한다() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("nickname", "testNickname");
        userData.put("password", "testPassword");
        userData.put("email", "test@test.com");


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_닉네임이_누락되면_회원가입에_실패해야한다() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("userId", "12345678901234567890");
        userData.put("password", "testPassword");
        userData.put("email", "test@test.com");


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_비밀번호가_누락되면_회원가입에_실패해야한다() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("userId", "12345678901234567890");
        userData.put("nickname", "testNickname");
        userData.put("email", "test@test.com");


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_이메일이_누락되면_회원가입에_실패해야한다() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("userId", "12345678901234567890");
        userData.put("nickname", "testNickname");
        userData.put("password", "testPassword");


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_아이디가_규칙에_맞지_않으면_회원가입에_실패해야한다() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("userId", "12345678901234567890X");
        userData.put("nickname", "testNickname");
        userData.put("password", "testPassword");
        userData.put("email", "test@test.com");


        ResultActions resultActions = sut.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userData)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_닉네임이_규칙에_맞지_않으면_회원가입에_실패해야한다() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("userId", "12345678901234567890");
        userData.put("nickname", "");
        userData.put("password", "testPassword");
        userData.put("email", "test@test.com");


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_비밀번호가_규칙에_맞지_않으면_회원가입에_실패해야한다() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("userId", "12345678901234567890");
        userData.put("nickname", "testNickname");
        userData.put("password", "");
        userData.put("email", "test@test.com");


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_이메일이_규칙에_맞지_않으면_회원가입에_실패해야한다() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("userId", "12345678901234567890");
        userData.put("nickname", "testNickname");
        userData.put("password", "testPassword");
        userData.put("email", "not email");


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)));


        resultActions.andExpect(status().isBadRequest());
    }
}