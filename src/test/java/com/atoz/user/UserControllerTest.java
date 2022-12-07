package com.atoz.user;

import com.atoz.error.GlobalExceptionAdvice;
import com.atoz.user.helper.SpyUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private SpyUserService userService;

    private MockMvc sut;

    @BeforeEach
    public void beforeEach() {
        userService = new SpyUserService();

        this.sut = MockMvcBuilders
                .standaloneSetup(new UserController(new Argon2PasswordEncoder(), userService))
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .setControllerAdvice(GlobalExceptionAdvice.class)
                .build();
    }

    @Test
    void signup_회원가입_요청에_성공한다() throws Exception {
        Map<String, String> signupRequest = new HashMap<>();
        signupRequest.put("userId", "12345678901234567890");
        signupRequest.put("nickname", "testNickname");
        signupRequest.put("password", "testPassword");
        signupRequest.put("email", "test@test.com");


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)));


        resultActions.andExpect(status().isOk());
    }

    @Test
    void signup_회원가입_요청시_비밀번호가_인코딩된다() throws Exception {
        Map<String, String> signupRequest = new HashMap<>();
        signupRequest.put("userId", "12345678901234567890");
        signupRequest.put("nickname", "testNickname");
        signupRequest.put("password", "testPassword");
        signupRequest.put("email", "test@test.com");


        ResultActions resultActions = sut.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)));


        PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();
        String encodedPassword = userService.encodedPassword;

        resultActions.andExpect(status().isOk());
        assertTrue(passwordEncoder.matches(signupRequest.get("password"), encodedPassword));
    }

    @Test
    void signup_아이디가_누락되면_회원가입_요청에_실패한다() throws Exception {
        Map<String, String> signupRequest = new HashMap<>();
        signupRequest.put("nickname", "testNickname");
        signupRequest.put("password", "testPassword");
        signupRequest.put("email", "test@test.com");


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_닉네임이_누락되면_회원가입_요청에_실패한다() throws Exception {
        Map<String, String> signupRequest = new HashMap<>();
        signupRequest.put("userId", "12345678901234567890");
        signupRequest.put("password", "testPassword");
        signupRequest.put("email", "test@test.com");


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_비밀번호가_누락되면_회원가입_요청에_실패한다() throws Exception {
        Map<String, String> signupRequest = new HashMap<>();
        signupRequest.put("userId", "12345678901234567890");
        signupRequest.put("nickname", "testNickname");
        signupRequest.put("email", "test@test.com");


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_이메일이_누락되면_회원가입_요청에_실패한다() throws Exception {
        Map<String, String> signupRequest = new HashMap<>();
        signupRequest.put("userId", "12345678901234567890");
        signupRequest.put("nickname", "testNickname");
        signupRequest.put("password", "testPassword");


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_아이디가_규칙에_맞지_않으면_회원가입_요청에_실패한다() throws Exception {
        Map<String, String> signupRequest = new HashMap<>();
        signupRequest.put("userId", "12345678901234567890X");
        signupRequest.put("nickname", "testNickname");
        signupRequest.put("password", "testPassword");
        signupRequest.put("email", "test@test.com");


        ResultActions resultActions = sut.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_닉네임이_규칙에_맞지_않으면_회원가입_요청에_실패한다() throws Exception {
        Map<String, String> signupRequest = new HashMap<>();
        signupRequest.put("userId", "12345678901234567890");
        signupRequest.put("nickname", "");
        signupRequest.put("password", "testPassword");
        signupRequest.put("email", "test@test.com");


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_비밀번호가_규칙에_맞지_않으면_회원가입_요청에_실패한다() throws Exception {
        Map<String, String> signupRequest = new HashMap<>();
        signupRequest.put("userId", "12345678901234567890");
        signupRequest.put("nickname", "testNickname");
        signupRequest.put("password", "");
        signupRequest.put("email", "test@test.com");


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_이메일이_규칙에_맞지_않으면_회원가입_요청에_실패한다() throws Exception {
        Map<String, String> signupRequest = new HashMap<>();
        signupRequest.put("userId", "12345678901234567890");
        signupRequest.put("nickname", "testNickname");
        signupRequest.put("password", "testPassword");
        signupRequest.put("email", "not email");


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void update_사용자_정보_변경_요청에_성공한다() throws Exception {
        Map<String, String> updateRequest = new HashMap<>();
        updateRequest.put("userId", "testUserId");
        updateRequest.put("nickname", "testNickname");
        updateRequest.put("email", "test@test.com");


        ResultActions resultActions = sut.perform(patch("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)));


        resultActions.andExpect(status().isOk());
    }

    @Test
    void changePassword_비밀번호_변경_요청시_비밀번호가_인코딩된다() throws Exception {
        Map<String, String> changePasswordRequest = new HashMap<>();
        changePasswordRequest.put("userId", "12345678901234567890");
        changePasswordRequest.put("password", "testPassword");


        ResultActions resultActions = sut.perform(patch("/user/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequest)));


        PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();
        String encodedPassword = userService.encodedPassword;

        resultActions.andExpect(status().isOk());
        assertTrue(passwordEncoder.matches(changePasswordRequest.get("password"), encodedPassword));
    }

    @Test
    void delete_회원탈퇴_요청에_성공한다() throws Exception {
        Map<String, String> deleteRequest = new HashMap<>();
        deleteRequest.put("userId", "testUserId");


        ResultActions resultActions = sut.perform(delete("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deleteRequest)));


        resultActions.andExpect(status().isOk());
    }
}