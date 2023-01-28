package com.atoz.comment.helper;

import com.atoz.comment.CommentMapper;
import com.atoz.comment.dto.domain.Comment;
import com.atoz.comment.dto.request.DeleteCommentRequestDto;
import com.atoz.comment.dto.request.LoadCommentsRequestDto;
import com.atoz.comment.dto.request.UpdateCommentRequestDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SpyCommentMapper implements CommentMapper {

    private Map<Long, Comment> storage = new HashMap<>();
    private Long id = 1L;
    private Comment capturedComment = null;

    @Override
    public List<Comment> loadComments(LoadCommentsRequestDto loadCommentsRequestDto) {
        return storage.values()
                .stream()
                .toList();
    }

    @Override
    public int addComment(Comment comment) {
        Comment commentWithCommentId = Comment.builder()
                .commentId(id)
                .parentCommentId(comment.getParentCommentId())
                .depth(comment.getDepth())
                .postId(comment.getPostId())
                .userId(comment.getUserId())
                .likeCount(comment.getLikeCount())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
        storage.put(id++, commentWithCommentId);

        capturedComment = commentWithCommentId;

        int affectedRows = 1;
        return affectedRows;
    }

    @Override
    public Optional<Comment> findCommentByCommentId(long commentId) {
        return Optional.ofNullable(storage.get(commentId));
    }

    @Override
    public int updateComment(long commentId, UpdateCommentRequestDto updateCommentRequestDto) {
        int affectedRows = 0;
        if (!storage.containsKey(commentId)) {
            return affectedRows;
        }

        Comment oldComment = storage.get(commentId);
        Comment updatedComment = Comment.builder()
                .commentId(oldComment.getCommentId())
                .parentCommentId(oldComment.getParentCommentId())
                .depth(oldComment.getDepth())
                .postId(oldComment.getPostId())
                .userId(oldComment.getUserId())
                .likeCount(oldComment.getLikeCount())
                .createdAt(oldComment.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        storage.put(commentId, updatedComment);

        affectedRows = 1;
        return affectedRows;
    }

    @Override
    public int increaseLikeCount(long commentId) {
        int affectedRows = 0;
        if (!storage.containsKey(commentId)) {
            return affectedRows;
        }

        Comment oldComment = storage.get(commentId);
        Comment updatedComment = Comment.builder()
                .commentId(oldComment.getCommentId())
                .parentCommentId(oldComment.getParentCommentId())
                .depth(oldComment.getDepth())
                .postId(oldComment.getPostId())
                .userId(oldComment.getUserId())
                .likeCount(oldComment.getLikeCount() + 1)
                .createdAt(oldComment.getCreatedAt())
                .updatedAt(oldComment.getUpdatedAt())
                .build();

        storage.put(commentId, updatedComment);

        affectedRows = 1;
        return affectedRows;
    }

    @Override
    public int deleteComment(long commentId, DeleteCommentRequestDto deleteCommentRequestDto) {
        int affectedRows = 0;
        if (!storage.containsKey(commentId)) {
            return affectedRows;
        }

        storage.remove(commentId);

        affectedRows = 1;
        return affectedRows;
    }

    public Comment cloneAndFlushCapturedComment() {
        Comment comment = Comment.builder()
                .commentId(capturedComment.getCommentId())
                .parentCommentId(capturedComment.getParentCommentId())
                .depth(capturedComment.getDepth())
                .postId(capturedComment.getPostId())
                .userId(capturedComment.getUserId())
                .likeCount(capturedComment.getLikeCount())
                .createdAt(capturedComment.getCreatedAt())
                .updatedAt(capturedComment.getUpdatedAt())
                .build();

        capturedComment = null;

        return comment;
    }
}
