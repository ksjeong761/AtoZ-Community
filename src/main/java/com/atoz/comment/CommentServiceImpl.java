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

    public LoadCommentsResponseDto loadComments(LoadCommentsRequestDto loadCommentsRequestDto) {
        List<Comment> comments = commentMapper.loadComments(loadCommentsRequestDto);
        return LoadCommentsResponseDto.builder()
                .comments(comments)
                .build();
    }

    @Override
    public void addComment(AddCommentRequestDto addCommentRequestDto, UserDetails userDetails) {
        commentMapper.addComment(addCommentRequestDto, userDetails.getUsername());
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
}