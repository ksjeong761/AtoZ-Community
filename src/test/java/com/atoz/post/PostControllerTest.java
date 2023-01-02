package com.atoz.post;

import com.atoz.error.GlobalExceptionAdvice;
import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.OpenPostRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.helper.StubPostService;
import com.atoz.security.authentication.helper.CustomWithMockUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostControllerTest {

    private MockMvc sut;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        sut = MockMvcBuilders
                .standaloneSetup(new PostController(new StubPostService()))
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .setControllerAdvice(GlobalExceptionAdvice.class)
                .build();
    }

    @Test
    void addPost_게시글_추가_요청에_성공한다() throws Exception {
        AddPostRequestDto addPostRequestDto = AddPostRequestDto.builder()
                .title("testTitle")
                .content("testContent")
                .build();


        ResultActions resultActions = sut.perform(post("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addPostRequestDto)));


        resultActions.andExpect(status().isOk());
    }

    @Test
    void updatePost_게시글_수정_요청에_성공한다() throws Exception {
        UpdatePostRequestDto updatePostRequestDto = UpdatePostRequestDto.builder()
                .postId(1)
                .userId("testUserId")
                .title("newTitle")
                .content("newContent")
                .build();


        ResultActions resultActions = sut.perform(patch("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePostRequestDto)));


        resultActions.andExpect(status().isOk());
    }

    @Test
    void deletePost_게시글_삭제_요청에_성공한다() throws Exception {
        DeletePostRequestDto deletePostRequestDto = DeletePostRequestDto.builder()
                .postId(1)
                .userId("testUserId")
                .build();


        ResultActions resultActions = sut.perform(delete("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deletePostRequestDto)));


        resultActions.andExpect(status().isOk());
    }

    @Test
    void openPost_게시글_열기_요청에_성공한다() throws Exception {
        OpenPostRequestDto openPostRequestDto = OpenPostRequestDto.builder()
                .postId(1)
                .build();


        ResultActions resultActions = sut.perform(get("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(openPostRequestDto)));


        resultActions.andExpect(status().isOk());
    }
}
