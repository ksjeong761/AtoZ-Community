package com.atoz.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserController userController;

    @Test
    @DisplayName("회원가입 테스트")
    void register_test() throws Exception {
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userId", "testUserId")
                .param("nickname", "testNickname")
                .param("password", "testPassword")
                .param("email", "test@test.com")
        ).andExpect(status().isOk());
    }
}