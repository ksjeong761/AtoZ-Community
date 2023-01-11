package com.atoz.post;

import com.atoz.error.GlobalExceptionAdvice;
import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.LoadPostsRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.helper.StubPostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
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
    void loadPosts_게시글_목록_가져오기_요청에_성공한다() throws Exception {
        LoadPostsRequestDto loadPostsRequestDto = LoadPostsRequestDto.builder()
                .offset(0)
                .limit(10)
                .build();


        ResultActions resultActions = sut.perform(
                get("/posts?offset={offset}&limit={limit}",
                    loadPostsRequestDto.getOffset(),
                    loadPostsRequestDto.getLimit()
                )
        );


        resultActions.andExpect(status().isOk());
    }

    @Test
    void addPost_게시글_추가_요청에_성공한다() throws Exception {
        AddPostRequestDto addPostRequestDto = AddPostRequestDto.builder()
                .title("testTitle")
                .content("testContent")
                .build();


        ResultActions resultActions = sut.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addPostRequestDto)));


        resultActions.andExpect(status().isOk());
    }

    @Test
    void updatePost_게시글_수정_요청에_성공한다() throws Exception {
        long postId = 1;
        UpdatePostRequestDto updatePostRequestDto = UpdatePostRequestDto.builder()
                .userId("testUserId")
                .title("newTitle")
                .content("newContent")
                .build();


        ResultActions resultActions = sut.perform(patch("/posts/{postId}", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePostRequestDto)));


        resultActions.andExpect(status().isOk());
    }

    @Test
    void deletePost_게시글_삭제_요청에_성공한다() throws Exception {
        long postId = 1;
        DeletePostRequestDto deletePostRequestDto = DeletePostRequestDto.builder()
                .userId("testUserId")
                .build();


        ResultActions resultActions = sut.perform(delete("/posts/{postId}", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deletePostRequestDto)));


        resultActions.andExpect(status().isOk());
    }

    @Test
    void openPost_게시글_열기_요청에_성공한다() throws Exception {
        long postId = 1;


        ResultActions resultActions = sut.perform(get("/posts/{postId}", postId)
                .contentType(MediaType.APPLICATION_JSON));


        resultActions.andExpect(status().isOk());
    }
}
