package com.atoz.user;

import com.atoz.error.GlobalExceptionAdvice;
import com.atoz.user.dto.request.ChangePasswordRequestDto;
import com.atoz.user.dto.request.DeleteUserRequestDto;
import com.atoz.user.dto.request.SignupRequestDto;
import com.atoz.user.dto.request.UpdateUserRequestDto;
import com.atoz.user.helper.SpyUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private SpyUserService userService;

    private MockMvc sut;

    @BeforeEach
    public void beforeEach() {
        userService = new SpyUserService();

        sut = MockMvcBuilders
                .standaloneSetup(new UserController(new Argon2PasswordEncoder(), userService))
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .setControllerAdvice(GlobalExceptionAdvice.class)
                .build();
    }

    @Test
    void signup_회원가입_요청에_성공한다() throws Exception {
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .build();


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDto)));


        resultActions.andExpect(status().isOk());
    }

    @Test
    void signup_회원가입_요청시_비밀번호가_인코딩된다() throws Exception {
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .build();


        ResultActions resultActions = sut.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequestDto)));


        PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();
        resultActions.andExpect(status().isOk());
        assertTrue(passwordEncoder.matches(signupRequestDto.getPassword(), userService.encodedPassword));
    }

    @Test
    void signup_아이디가_누락되면_회원가입_요청에_실패한다() throws Exception {
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .build();


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_닉네임이_누락되면_회원가입_요청에_실패한다() throws Exception {
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .userId("testUserId")
                .password("testPassword")
                .email("test@test.com")
                .build();


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_비밀번호가_누락되면_회원가입_요청에_실패한다() throws Exception {
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .userId("testUserId")
                .nickname("testNickname")
                .email("test@test.com")
                .build();


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_이메일이_누락되면_회원가입_요청에_실패한다() throws Exception {
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .build();


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_아이디가_규칙에_맞지_않으면_회원가입_요청에_실패한다() throws Exception {
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .userId("12345678901234567890_")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .build();


        ResultActions resultActions = sut.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_닉네임이_규칙에_맞지_않으면_회원가입_요청에_실패한다() throws Exception {
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .userId("testUserId")
                .password("testPassword")
                .nickname("")
                .email("test@test.com")
                .build();


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_비밀번호가_규칙에_맞지_않으면_회원가입_요청에_실패한다() throws Exception {
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .userId("testUserId")
                .password("")
                .nickname("testNickname")
                .email("test@test.com")
                .build();


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void signup_이메일이_규칙에_맞지_않으면_회원가입_요청에_실패한다() throws Exception {
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("not email")
                .build();


        ResultActions resultActions = sut.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void update_사용자_정보_변경_요청에_성공한다() throws Exception {
        UpdateUserRequestDto updateUserRequestDto = UpdateUserRequestDto.builder()
                .userId("testUserId")
                .nickname("testNickname")
                .email("test@test.com")
                .build();


        ResultActions resultActions = sut.perform(patch("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserRequestDto)));


        resultActions.andExpect(status().isOk());
    }

    @Test
    void changePassword_비밀번호_변경_요청시_비밀번호가_인코딩된다() throws Exception {
        ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
                .userId("testUserId")
                .password("testPassword")
                .build();


        ResultActions resultActions = sut.perform(patch("/user/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequestDto)));


        PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();
        resultActions.andExpect(status().isOk());
        assertTrue(passwordEncoder.matches(changePasswordRequestDto.getPassword(), userService.encodedPassword));
    }

    @Test
    void delete_회원탈퇴_요청에_성공한다() throws Exception {
        DeleteUserRequestDto deleteUserRequestDto = DeleteUserRequestDto.builder()
                .userId("testUserId")
                .build();


        ResultActions resultActions = sut.perform(delete("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deleteUserRequestDto)));


        resultActions.andExpect(status().isOk());
    }
}