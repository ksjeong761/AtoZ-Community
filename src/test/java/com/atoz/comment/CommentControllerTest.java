package com.atoz.comment;

import com.atoz.comment.helper.SpyCommentService;
import com.atoz.error.GlobalExceptionAdvice;
import com.atoz.comment.dto.request.AddCommentRequestDto;
import com.atoz.comment.dto.request.DeleteCommentRequestDto;
import com.atoz.comment.dto.request.LoadCommentsRequestDto;
import com.atoz.comment.dto.request.UpdateCommentRequestDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class CommentControllerTest {

    private MockMvc sut;
    private SpyCommentService commentService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String USER_ID = "testUserId";

    @BeforeEach
    void setUp() {
        commentService = new SpyCommentService();
        sut = MockMvcBuilders
                .standaloneSetup(new CommentController(commentService))
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .setControllerAdvice(GlobalExceptionAdvice.class)
                .build();
    }

    @Test
    void loadComments_댓글_목록_조회_요청에_성공한다() throws Exception {
        LoadCommentsRequestDto loadCommentsRequestDto = LoadCommentsRequestDto.builder()
                .postId(99)
                .build();


        ResultActions resultActions = sut.perform(
                get("/comments?postId={postId}", loadCommentsRequestDto.getPostId())
        );


        resultActions.andExpect(status().isOk());
    }

    @Test
    void loadComments_전달된_패러미터가_DTO에_매핑된다() throws Exception {
        LoadCommentsRequestDto loadCommentsRequestDto = LoadCommentsRequestDto.builder()
                .postId(99)
                .build();


        sut.perform(get("/comments?postId={postId}", loadCommentsRequestDto.getPostId()));


        assertEquals(loadCommentsRequestDto.getPostId(), commentService.capturedLoadCommentsRequestDto.getPostId());
    }

    @Test
    void loadComments_게시글_번호_패러미터가_누락되면_400을_응답한다() throws Exception {
        ResultActions resultActions = sut.perform(
                get("/comments")
        );


        resultActions.andExpect(status().isBadRequest());
    }
}