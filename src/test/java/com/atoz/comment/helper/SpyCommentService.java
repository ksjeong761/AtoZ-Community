package com.atoz.comment.helper;

import com.atoz.comment.CommentService;
import com.atoz.comment.dto.domain.Comment;
import com.atoz.comment.dto.request.AddCommentRequestDto;
import com.atoz.comment.dto.request.DeleteCommentRequestDto;
import com.atoz.comment.dto.request.LoadCommentsRequestDto;
import com.atoz.comment.dto.request.UpdateCommentRequestDto;
import com.atoz.comment.dto.response.LoadCommentsResponseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SpyCommentService implements CommentService {

    public LoadCommentsRequestDto capturedLoadCommentsRequestDto = null;
    public AddCommentRequestDto capturedAddCommentRequestDto = null;
    public String capturedUserId = null;

    @Override
    public LoadCommentsResponseDto loadComments(LoadCommentsRequestDto loadCommentsRequestDto) {
        this.capturedLoadCommentsRequestDto = loadCommentsRequestDto;

        return LoadCommentsResponseDto.builder()
                .comments(this.generateComments(3))
                .build();
    }

    @Override
    public void addComment(AddCommentRequestDto addCommentRequestDto, String userId) {
        this.capturedAddCommentRequestDto = addCommentRequestDto;
        this.capturedUserId = userId;
    }

    @Override
    public void updateComment(long commentId, UpdateCommentRequestDto updateCommentRequestDto) {

    }

    @Override
    public void increaseLikeCount(long commentId) {

    }

    @Override
    public void deleteComment(long commentId, DeleteCommentRequestDto deleteCommentRequestDto) {

    }

    private List<Comment> generateComments(int size) {
        List<Comment> comments = new ArrayList<>();

        for (int i = 1; i <= size; i++) {
            Comment comment = Comment.builder()
                    .commentId(i)
                    .parentCommentId(0)
                    .depth(1)
                    .postId(1)
                    .userId("testUserId" + i)
                    .content("testComment" + i)
                    .likeCount(0)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            comments.add(comment);
        }

        return comments;
    }
}