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

    int addComment(@Param("addCommentRequestDto") AddCommentRequestDto addCommentRequestDto,
                    @Param("userId") String userId);

    int updateComment(@Param("commentId") long commentId,
                       @Param("updateCommentRequestDto") UpdateCommentRequestDto updateCommentRequestDto);

    int increaseLikeCount(long commentId);

    int deleteComment(@Param("commentId") long commentId,
                       @Param("deleteCommentRequestDto") DeleteCommentRequestDto deleteCommentRequestDto);
}