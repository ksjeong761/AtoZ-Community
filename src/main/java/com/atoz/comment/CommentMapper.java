package com.atoz.comment;

import com.atoz.comment.dto.domain.Comment;
import com.atoz.comment.dto.request.AddCommentRequestDto;
import com.atoz.comment.dto.request.DeleteCommentRequestDto;
import com.atoz.comment.dto.request.LoadCommentsRequestDto;
import com.atoz.comment.dto.request.UpdateCommentRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<Comment> loadComments(LoadCommentsRequestDto loadCommentsRequestDto);

    void addComment(AddCommentRequestDto addCommentRequestDto);

    void updateComment(@Param("commentId") long commentId,
                       @Param("updateCommentRequestDto") UpdateCommentRequestDto updateCommentRequestDto);

    void increaseLikeCount(long commentId);

    void deleteComment(@Param("commentId") long commentId,
                       @Param("deleteCommentRequestDto") DeleteCommentRequestDto deleteCommentRequestDto);
}