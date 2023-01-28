package com.atoz.comment;

import com.atoz.comment.dto.request.AddCommentRequestDto;
import com.atoz.comment.dto.request.DeleteCommentRequestDto;
import com.atoz.comment.dto.request.LoadCommentsRequestDto;
import com.atoz.comment.dto.request.UpdateCommentRequestDto;
import com.atoz.comment.dto.response.LoadCommentsResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface CommentService {

    LoadCommentsResponseDto loadComments(LoadCommentsRequestDto loadCommentsRequestDto);

    void addComment(AddCommentRequestDto addCommentRequestDto, String userId);

    void updateComment(long commentId, UpdateCommentRequestDto updateCommentRequestDto);

    void increaseLikeCount(long commentId);

    void deleteComment(long commentId, DeleteCommentRequestDto deleteCommentRequestDto);
}