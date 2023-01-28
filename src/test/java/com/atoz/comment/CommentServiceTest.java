package com.atoz.comment;

import com.atoz.comment.dto.domain.Comment;
import com.atoz.comment.dto.request.AddCommentRequestDto;
import com.atoz.comment.helper.SpyCommentMapper;
import com.atoz.error.exception.NoRowsFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

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


        sut.addComment(childCommentRequestDto, "testUserId");


        Comment childComment = commentMapper.cloneAndFlushCapturedComment();
        assertEquals(parentComment.getDepth() + 1, childComment.getDepth());
    }

    @Test
    void addComment_존재하지_않는_부모_댓글에_대댓글을_추가하면_예외가_발생한다() {
        AddCommentRequestDto childCommentRequestDto = AddCommentRequestDto.builder()
                .parentCommentId(99999)
                .postId(1)
                .content("child content")
                .build();


        Throwable thrown = catchThrowable(() -> {
            sut.addComment(childCommentRequestDto, "testUserId");
        });


        assertInstanceOf(NoRowsFoundException.class, thrown);
    }

    private Comment addParentComment() {
        AddCommentRequestDto addCommentRequestDto = AddCommentRequestDto.builder()
                .parentCommentId(0)
                .postId(1)
                .content("parent content")
                .build();
        sut.addComment(addCommentRequestDto, "testUserId");

        return commentMapper.cloneAndFlushCapturedComment();
    }
}