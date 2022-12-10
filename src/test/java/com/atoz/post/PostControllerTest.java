package com.atoz.post;

import com.atoz.error.GlobalExceptionAdvice;
import com.atoz.post.helper.StubPostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostControllerTest {

    MockMvc sut;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        sut = MockMvcBuilders
                .standaloneSetup(new PostController(new StubPostService()))
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .setControllerAdvice(GlobalExceptionAdvice.class)
                .build();
    }

    @Test
    void addPost_게시글_추가_요청에_성공한다() throws Exception {
        Map<String, String> addRequest = new HashMap<>();
        addRequest.put("title", "testTitle");
        addRequest.put("content", "testContent");


        ResultActions resultActions = sut.perform(put("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addRequest)));


        resultActions.andExpect(status().isOk());
    }

    @Test
    void updatePost_게시글_수정_요청에_성공한다() throws Exception {
        Map<String, String> updateRequest = new HashMap<>();
        updateRequest.put("postId", "1");
        updateRequest.put("title", "newTitle");
        updateRequest.put("content", "newContent");


        ResultActions resultActions = sut.perform(patch("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)));


        resultActions.andExpect(status().isOk());
    }

    @Test
    void deletePost_게시글_삭제_요청에_성공한다() throws Exception {
        Map<String, String> deleteRequest = new HashMap<>();
        deleteRequest.put("postId", "1");


        ResultActions resultActions = sut.perform(delete("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deleteRequest)));


        resultActions.andExpect(status().isOk());
    }

    @Test
    void openPost_게시글_열기_요청에_성공한다() throws Exception {
        Map<String, String> openRequest = new HashMap<>();
        openRequest.put("postId", "1");


        ResultActions resultActions = sut.perform(get("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(openRequest)));


        resultActions.andExpect(status().isOk());
    }
}
