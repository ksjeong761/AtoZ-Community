package com.atoz.comment;

import com.atoz.comment.dto.domain.Comment;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@TestPropertySource(locations = "/application-test.yaml")
@MybatisTest
class CommentMapperTest {

    @Autowired
    private CommentMapper sut;

    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserMapper userMapper;

    private long addedPostId = -1;

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
        this.addedPostId = addPostRequestDto.getPostId();
    }

    @Test
    void loadComments_게시글에_등록된_댓글_목록이_조회된다() {
        int size = 10;
        long lastAddedCommentId = this.addSampleComments(size);
        LoadCommentsRequestDto loadCommentsRequestDto = LoadCommentsRequestDto.builder()
                .postId(addedPostId)
                .build();


        List<Comment> result = sut.loadComments(loadCommentsRequestDto);


        assertEquals(size, result.size());
        assertEquals(lastAddedCommentId, result.get(9).getCommentId());
        assertEquals(addedPostId, result.get(9).getPostId());
        assertEquals("sample content", result.get(9).getContent());
        assertEquals("testUserId", result.get(9).getUserId());
    }

    @Test
    void addComment_댓글이_저장된다() {
        Comment comment = Comment.builder()
                .parentCommentId(0)
                .depth(1)
                .postId(addedPostId)
                .userId("testUserId")
                .content("sample content")
                .likeCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


        int affectedRowCount = sut.addComment(comment);


        List<Comment> result = this.loadAddedComments();
        assertEquals(comment.getContent(), result.get(0).getContent());
        assertEquals(1, affectedRowCount);
    }

    @Test
    void addComment_존재하지_않는_게시글에_댓글을_저장하면_무결성_위반_예외가_발생한다() {
        long nonexistentPostId = 99999;
        Comment comment = Comment.builder()
                .parentCommentId(0)
                .depth(1)
                .postId(nonexistentPostId)
                .userId("testUserId")
                .content("sample content")
                .likeCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


        Throwable thrown = catchThrowable(() -> {
            sut.addComment(comment);
        });


        assertInstanceOf(DataIntegrityViolationException.class, thrown);
    }

    @Test
    void updateComment_댓글_내용이_수정된다() {
        long commentId = this.addSampleComment().getCommentId();
        UpdateCommentRequestDto updateCommentRequestDto = UpdateCommentRequestDto.builder()
                .postId(addedPostId)
                .userId("testUserId")
                .content("updated content")
                .build();


        int affectedRowCount = sut.updateComment(commentId, updateCommentRequestDto);


        Comment result = this.loadAddedComments().get(0);
        assertEquals(updateCommentRequestDto.getContent(), result.getContent());
        assertEquals(1, affectedRowCount);
    }

    @Test
    void updateComment_댓글이_존재하지_않으면_댓글이_수정되지_않는다() {
        long commentId = 99999;
        UpdateCommentRequestDto updateCommentRequestDto = UpdateCommentRequestDto.builder()
                .postId(addedPostId)
                .userId("testUserId")
                .content("updated content")
                .build();


        int affectedRowCount = sut.updateComment(commentId, updateCommentRequestDto);


        assertEquals(0, affectedRowCount);
    }

    @Test
    void updateComment_게시글_번호가_존재하지_않으면_댓글이_수정되지_않는다() {
        long nonexistentPostId = 99999;
        long commentId = this.addSampleComment().getCommentId();
        UpdateCommentRequestDto updateCommentRequestDto = UpdateCommentRequestDto.builder()
                .postId(nonexistentPostId)
                .userId("testUserId")
                .content("updated content")
                .build();


        int affectedRowCount = sut.updateComment(commentId, updateCommentRequestDto);


        assertEquals(0, affectedRowCount);
    }

    @Test
    void increaseLikeCount_좋아요_갯수가_증가한다() {
        long commentId = this.addSampleComment().getCommentId();


        int affectedRowCount = sut.increaseLikeCount(commentId);


        Comment result = this.loadAddedComments().get(0);
        assertEquals(1, result.getLikeCount());
        assertEquals(1, affectedRowCount);
    }

    @Test
    void increaseLikeCount_게시글_번호가_존재하지_않으면_좋아요_갯수가_증가하지_않는다() {
        long nonexistentCommentId = 99999;


        int affectedRowCount = sut.increaseLikeCount(nonexistentCommentId);


        assertEquals(0, affectedRowCount);
    }

    @Test
    void deleteComment_댓글이_삭제된다() {
        int size = 10;
        long commentId = this.addSampleComments(size);
        DeleteCommentRequestDto deleteCommentRequestDto = DeleteCommentRequestDto.builder()
                .userId("testUserId")
                .build();


        int affectedRowCount = sut.deleteComment(commentId, deleteCommentRequestDto);


        List<Comment> result = this.loadAddedComments();
        assertEquals(9, result.size());
        assertEquals(1, affectedRowCount);
    }

    @Test
    void deleteComment_댓글이_존재하지_않으면_댓글이_삭제되지_않는다() {
        long commentId = 99999;
        DeleteCommentRequestDto deleteCommentRequestDto = DeleteCommentRequestDto.builder()
                .userId("testUserId")
                .build();


        int affectedRowCount = sut.deleteComment(commentId, deleteCommentRequestDto);


        assertEquals(0, affectedRowCount);
    }

    private Comment addSampleComment() {
        Comment sampleComment = Comment.builder()
                .parentCommentId(0)
                .depth(1)
                .postId(addedPostId)
                .userId("testUserId")
                .content("sample content")
                .likeCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        sut.addComment(sampleComment);

        return sampleComment;
    }

    private long addSampleComments(int size) {
        long lastCommentId = -1;
        for (int i = 0; i < size; i++) {
            lastCommentId = this.addSampleComment().getCommentId();
        }

        return lastCommentId;
    }

    private List<Comment> loadAddedComments() {
        LoadCommentsRequestDto loadCommentsRequestDto = LoadCommentsRequestDto.builder()
                .postId(addedPostId)
                .build();
        return sut.loadComments(loadCommentsRequestDto);
    }
}