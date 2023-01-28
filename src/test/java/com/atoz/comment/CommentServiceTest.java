package com.atoz.comment;

import com.atoz.comment.dto.domain.Comment;
import com.atoz.comment.dto.request.AddCommentRequestDto;
import com.atoz.comment.helper.SpyCommentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentServiceTest {

    private SpyCommentMapper commentMapper;
    private CommentService sut;

    @BeforeEach
    void setUp() {
        commentMapper = new SpyCommentMapper();
        sut = new CommentServiceImpl(commentMapper);
    }

    @Test
    void addComment_자식_댓글의_깊이는_부모_댓글의_깊이에_1을_더한_수치가_된다() {
        Comment parentComment = this.addParentComment();
        AddCommentRequestDto childCommentRequestDto = AddCommentRequestDto.builder()
                .parentCommentId(parentComment.getCommentId())
                .postId(parentComment.getPostId())
                .content("child content")
                .build();
        UserDetails principal = User.builder()
                .username("testUserId")
                .password("testPassword")
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_FAKE")))
                .build();


        sut.addComment(childCommentRequestDto, principal);


        Comment childComment = commentMapper.cloneAndFlushCapturedComment();
        assertEquals(parentComment.getDepth() + 1, childComment.getDepth());
    }

    private Comment addParentComment() {
        AddCommentRequestDto addCommentRequestDto = AddCommentRequestDto.builder()
                .parentCommentId(0)
                .postId(1)
                .content("parent content")
                .build();
        UserDetails principal = User.builder()
                .username("testUserId")
                .password("testPassword")
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_FAKE")))
                .build();
        sut.addComment(addCommentRequestDto, principal);

        return commentMapper.cloneAndFlushCapturedComment();
    }
}