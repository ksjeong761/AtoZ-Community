package com.atoz.user;

import com.atoz.ControllerExceptionAdvice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService))
                .setControllerAdvice(new ControllerExceptionAdvice())
                .build();
    }

    @Test
    void 회원가입_요청_성공() throws Exception {
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
    void 회원_정보가_없으면_400_응답과_입력_요청_메시지() throws Exception {
        Map<String, String> userData = new HashMap<>();

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andExpect(jsonPath("$.errorMessages.length()").value(4));
    }

    @Test
    void 회원_정보가_누락되면_400_응답과_입력_요청_메시지() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("userId", "12345678901234567890");
        userData.put("email", "test@test.com");

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andExpect(jsonPath("$.errorMessages.length()").value(2));
    }

    @Test
    void 회원_정보가_규칙에_맞지_않으면_400_응답과_알림_메시지() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("userId", "12345678901234567890x");
        userData.put("nickname", "");
        userData.put("password", "correct password");
        userData.put("email", "not email");

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.errorMessages.length()").value(4));
    }

}