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

    @Test
    void loadComments_게시글_번호가_long타입_최대치를_넘기면_400을_응답한다() throws Exception {
        String tooBigPostId = "9223372036854775808L";


        ResultActions resultActions = sut.perform(
                get("/comments?postId={postId}", tooBigPostId)
        );


        resultActions.andExpect(status().isBadRequest());
    }

    @CustomWithMockUser(username = USER_ID)
    @Test
    void addComment_댓글_추가_요청에_성공한다() throws Exception {
        AddCommentRequestDto addCommentRequestDto = AddCommentRequestDto.builder()
                .postId(1)
                .parentCommentId(1)
                .content("testComment")
                .build();


        ResultActions resultActions = sut.perform(post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addCommentRequestDto)));


        resultActions.andExpect(status().isOk());
    }

    @CustomWithMockUser(username = USER_ID)
    @Test
    void addComment_댓글_추가_요청을_보낸_사용자_아이디를_SecurityContextHolder에서_가져온다() throws Exception {
        AddCommentRequestDto addCommentRequestDto = AddCommentRequestDto.builder()
                .postId(1)
                .parentCommentId(1)
                .content("testComment")
                .build();


        sut.perform(post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addCommentRequestDto)));


        assertEquals(USER_ID, commentService.capturedUserId);
    }

    @CustomWithMockUser(username = USER_ID)
    @Test
    void addComment_게시글_번호가_누락되면_400을_응답한다() throws Exception {
        AddCommentRequestDto addCommentRequestDto = AddCommentRequestDto.builder()
                .parentCommentId(1)
                .content("testComment")
                .build();


        ResultActions resultActions = sut.perform(post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addCommentRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    @CustomWithMockUser(username = USER_ID)
    @Test
    void addComment_부모_댓글_아이디가_누락되면_기본값이_사용된다() throws Exception {
        AddCommentRequestDto addCommentRequestDto = AddCommentRequestDto.builder()
                .postId(1)
                .content("testComment")
                .build();


        ResultActions resultActions = sut.perform(post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addCommentRequestDto)));


        AddCommentRequestDto defaultObject = new AddCommentRequestDto();
        AddCommentRequestDto capturedObject = commentService.capturedAddCommentRequestDto;
        assertEquals(defaultObject.getParentCommentId(), capturedObject.getParentCommentId());
        resultActions.andExpect(status().isOk());
    }

    @CustomWithMockUser(username = USER_ID)
    @Test
    void addComment_댓글_내용이_누락되면_400을_응답한다() throws Exception {
        AddCommentRequestDto addCommentRequestDto = AddCommentRequestDto.builder()
                .postId(1)
                .parentCommentId(1)
                .build();


        ResultActions resultActions = sut.perform(post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addCommentRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    @CustomWithMockUser(username = USER_ID)
    @Test
    void addComment_댓글이_너무_길면_400을_응답한다() throws Exception {
        String tooLongContent = this.generateString(512);
        AddCommentRequestDto addCommentRequestDto = AddCommentRequestDto.builder()
                .postId(1)
                .parentCommentId(1)
                .content(tooLongContent)
                .build();


        ResultActions resultActions = sut.perform(post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addCommentRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void updateComment_댓글_수정_요청에_성공한다() throws Exception {
        long commentId = 1;
        UpdateCommentRequestDto updateCommentRequestDto = UpdateCommentRequestDto.builder()
                .postId(1)
                .userId("testUserId")
                .content("testContent")
                .build();


        ResultActions resultActions = sut.perform(patch("/comments/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCommentRequestDto)));


        resultActions.andExpect(status().isOk());
    }


    @Test
    void updateComment_댓글_아이디가_long_타입_최대치를_넘으면_400을_응답한다() throws Exception {
        String tooBigCommentId = "9223372036854775808L";
        UpdateCommentRequestDto updateCommentRequestDto = UpdateCommentRequestDto.builder()
                .postId(1)
                .userId("testUserId")
                .content("testContent")
                .build();


        ResultActions resultActions = sut.perform(patch("/comments/{commentId}", tooBigCommentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCommentRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void updateComment_게시글_번호가_누락되면_400을_응답한다() throws Exception {
        long commentId = 1;
        UpdateCommentRequestDto updateCommentRequestDto = UpdateCommentRequestDto.builder()
                .userId("testUserId")
                .content("testContent")
                .build();


        ResultActions resultActions = sut.perform(patch("/comments/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCommentRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void updateComment_댓글_작성자_아이디가_누락되면_400을_응답한다() throws Exception {
        long commentId = 1;
        UpdateCommentRequestDto updateCommentRequestDto = UpdateCommentRequestDto.builder()
                .postId(1)
                .content("testContent")
                .build();


        ResultActions resultActions = sut.perform(patch("/comments/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCommentRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void updateComment_댓글_내용이_누락되면_400을_응답한다() throws Exception {
        long commentId = 1;
        UpdateCommentRequestDto updateCommentRequestDto = UpdateCommentRequestDto.builder()
                .postId(1)
                .userId("testUserId")
                .build();


        ResultActions resultActions = sut.perform(patch("/comments/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCommentRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void updateComment_댓글_내용이_511자를_초과하면_400을_응답한다() throws Exception {
        String tooLongContent = this.generateString(512);
        long commentId = 1;
        UpdateCommentRequestDto updateCommentRequestDto = UpdateCommentRequestDto.builder()
                .postId(1)
                .userId("testUserId")
                .content(tooLongContent)
                .build();


        ResultActions resultActions = sut.perform(patch("/comments/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCommentRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void updateComment_댓글_작성자_아이디가_20자를_초과하면_400을_응답한다() throws Exception {
        String tooLongUserId = this.generateString(21);
        long commentId = 1;
        UpdateCommentRequestDto updateCommentRequestDto = UpdateCommentRequestDto.builder()
                .postId(1)
                .userId(tooLongUserId)
                .content("testContent")
                .build();


        ResultActions resultActions = sut.perform(patch("/comments/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCommentRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void increaseLikeCount_좋아요_요청에_성공한다() throws Exception {
        long commentId = 1;


        ResultActions resultActions = sut.perform(patch("/comments/{commentId}/like_count", commentId));


        resultActions.andExpect(status().isOk());
    }

    @Test
    void deleteComment_댓글_삭제_요청에_성공한다() throws Exception {
        long commentId = 1;
        DeleteCommentRequestDto deleteCommentRequestDto = DeleteCommentRequestDto.builder()
                .userId("testUserId")
                .build();


        ResultActions resultActions = sut.perform(delete("/comments/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deleteCommentRequestDto)));


        resultActions.andExpect(status().isOk());
    }

    @Test
    void deleteComment_댓글_작성자_아이디가_20자를_초과하면_400을_응답한다() throws Exception {
        String tooLongUserId = this.generateString(21);
        long commentId = 1;
        DeleteCommentRequestDto deleteCommentRequestDto = DeleteCommentRequestDto.builder()
                .userId(tooLongUserId)
                .build();


        ResultActions resultActions = sut.perform(patch("/comments/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deleteCommentRequestDto)));


        resultActions.andExpect(status().isBadRequest());
    }

    private String generateString(int size) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            stringBuilder.append('A');
        }

        return stringBuilder.toString();
    }
}