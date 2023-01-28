package com.atoz.comment;

import com.atoz.comment.dto.domain.Comment;
import com.atoz.comment.dto.request.AddCommentRequestDto;
import com.atoz.comment.dto.request.DeleteCommentRequestDto;
import com.atoz.comment.dto.request.LoadCommentsRequestDto;
import com.atoz.comment.dto.request.UpdateCommentRequestDto;
import com.atoz.comment.dto.response.LoadCommentsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    @Override
    public LoadCommentsResponseDto loadComments(LoadCommentsRequestDto loadCommentsRequestDto) {
        List<Comment> comments = commentMapper.loadComments(loadCommentsRequestDto);
        return LoadCommentsResponseDto.builder()
                .comments(comments)
                .build();
    }

    @Override
    public void addComment(AddCommentRequestDto addCommentRequestDto, UserDetails userDetails) {
        Comment comment = Comment.builder()
                .postId(addCommentRequestDto.getPostId())
                .parentCommentId(addCommentRequestDto.getParentCommentId())
                .userId(userDetails.getUsername())
                .depth(determineCommentDepth(addCommentRequestDto.getParentCommentId()))
                .content(addCommentRequestDto.getContent())
                .build();

        commentMapper.addComment(comment);
    }

    @Override
    public void updateComment(long commentId, UpdateCommentRequestDto updateCommentRequestDto) {
        commentMapper.updateComment(commentId, updateCommentRequestDto);
    }

    @Override
    public void increaseLikeCount(long commentId) {
        commentMapper.increaseLikeCount(commentId);
    }

    @Override
    public void deleteComment(long commentId, DeleteCommentRequestDto deleteCommentRequestDto) {
        commentMapper.deleteComment(commentId, deleteCommentRequestDto);
    }

    private int determineCommentDepth(long parentCommentId) {
        return commentMapper.findCommentByCommentId(parentCommentId)
                .map(parentComment -> parentComment.getDepth() + 1)
                .orElse(1);
    }
}