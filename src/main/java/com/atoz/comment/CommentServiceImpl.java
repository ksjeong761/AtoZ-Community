package com.atoz.comment;

import com.atoz.comment.dto.domain.Comment;
import com.atoz.comment.dto.request.AddCommentRequestDto;
import com.atoz.comment.dto.request.DeleteCommentRequestDto;
import com.atoz.comment.dto.request.LoadCommentsRequestDto;
import com.atoz.comment.dto.request.UpdateCommentRequestDto;
import com.atoz.comment.dto.response.LoadCommentsResponseDto;
import lombok.RequiredArgsConstructor;
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
    public void addComment(AddCommentRequestDto addCommentRequestDto) {
        commentMapper.addComment(addCommentRequestDto);
    }

    @Override
    public void updateComment(UpdateCommentRequestDto updateCommentRequestDto) {
        commentMapper.updateComment(updateCommentRequestDto);
    }

    @Override
    public void increaseLikeCount(long commentId) {
        commentMapper.increaseLikeCount(commentId);
    }

    @Override
    public void deleteComment(DeleteCommentRequestDto deleteCommentRequestDto) {
        commentMapper.deleteComment(deleteCommentRequestDto);
    }
}