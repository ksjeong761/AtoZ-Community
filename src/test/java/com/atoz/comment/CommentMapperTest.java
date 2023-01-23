package com.atoz.comment;

import com.atoz.comment.dto.domain.Comment;
import com.atoz.comment.dto.request.AddCommentRequestDto;
import com.atoz.comment.dto.request.DeleteCommentRequestDto;
import com.atoz.comment.dto.request.LoadCommentsRequestDto;
import com.atoz.comment.dto.request.UpdateCommentRequestDto;
import com.atoz.post.PostMapper;
import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.user.Authority;
import com.atoz.user.UserMapper;
import com.atoz.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource(locations = "/application-test.yaml")
@MybatisTest
class CommentMapperTest {

    @Autowired
    private CommentMapper sut;

    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserMapper userMapper;

    private long postId = -1;

    @BeforeEach
    void setUp() {
        // 외래키 제약조건 때문에 회원가입이 되어 있어야 게시글을 조작할 수 있습니다.
        UserDto signedUpUser = UserDto.builder()
                .userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        userMapper.addUser(signedUpUser);
        userMapper.addAuthority(signedUpUser);

        // 외래키 제약조건 때문에 게시글이 존재해야 댓글을 조작할 수 있습니다.
        AddPostRequestDto addPostRequestDto = AddPostRequestDto.builder()
                .title("testTitle")
                .content("testContent")
                .build();
        postMapper.addPost(addPostRequestDto, signedUpUser.getUserId());
        this.postId = addPostRequestDto.getPostId();
    }

    @Test
    void loadComments_게시글에_등록된_댓글_목록이_조회된다() {
        int size = 10;
        long lastAddedCommentId = this.addSampleComments(size);
        LoadCommentsRequestDto loadCommentsRequestDto = LoadCommentsRequestDto.builder()
                .postId(postId)
                .build();


        List<Comment> result = sut.loadComments(loadCommentsRequestDto);


        assertEquals(size, result.size());
        assertEquals(lastAddedCommentId, result.get(9).getCommentId());
        assertEquals(postId, result.get(9).getPostId());
        assertEquals("sample content", result.get(9).getContent());
        assertEquals("testUserId", result.get(9).getUserId());
    }

    @Test
    void addComment_댓글이_저장된다() {
        AddCommentRequestDto addCommentRequestDto = AddCommentRequestDto.builder()
                .parentCommentId(0)
                .postId(postId)
                .content("sample content")
                .build();


        sut.addComment(addCommentRequestDto, "testUserId");


        List<Comment> result = this.loadAddedComments();
        assertEquals(addCommentRequestDto.getContent(), result.get(0).getContent());
    }

    @Test
    void updateComment_댓글_내용이_수정된다() {
        long commentId = this.addSampleComments(1);
        UpdateCommentRequestDto updateCommentRequestDto = UpdateCommentRequestDto.builder()
                .postId(postId)
                .userId("testUserId")
                .content("updated content")
                .build();


        sut.updateComment(commentId, updateCommentRequestDto);


        Comment result = this.loadAddedComments().get(0);
        assertEquals(updateCommentRequestDto.getContent(), result.getContent());
    }

    @Test
    void increaseLikeCount_좋아요_갯수가_증가한다() {
        long commentId = this.addSampleComments(1);


        sut.increaseLikeCount(commentId);


        Comment result = this.loadAddedComments().get(0);
        assertEquals(1, result.getLikeCount());
    }

    @Test
    void deleteComment_댓글이_삭제된다() {
        int size = 10;
        long commentId = this.addSampleComments(size);
        DeleteCommentRequestDto deleteCommentRequestDto = DeleteCommentRequestDto.builder()
                .userId("testUserId")
                .build();


        sut.deleteComment(commentId, deleteCommentRequestDto);


        List<Comment> result = this.loadAddedComments();
        assertEquals(size - 1, result.size());
    }

    private long addSampleComments(int size) {
        AddCommentRequestDto addCommentRequestDto = AddCommentRequestDto.builder()
                .parentCommentId(0)
                .postId(postId)
                .content("sample content")
                .build();

        long lastCommentId = -1;
        for (int i = 0; i < size; i++) {
            sut.addComment(addCommentRequestDto, "testUserId");

            lastCommentId = addCommentRequestDto.getCommentId();
        }

        return lastCommentId;
    }

    private List<Comment> loadAddedComments() {
        LoadCommentsRequestDto loadCommentsRequestDto = LoadCommentsRequestDto.builder()
                .postId(postId)
                .build();
        return sut.loadComments(loadCommentsRequestDto);
    }
}