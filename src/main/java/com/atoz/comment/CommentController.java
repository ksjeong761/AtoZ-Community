package com.atoz.comment;

import com.atoz.comment.dto.request.AddCommentRequestDto;
import com.atoz.comment.dto.request.DeleteCommentRequestDto;
import com.atoz.comment.dto.request.LoadCommentsRequestDto;
import com.atoz.comment.dto.request.UpdateCommentRequestDto;
import com.atoz.comment.dto.response.LoadCommentsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public LoadCommentsResponseDto loadComments(LoadCommentsRequestDto loadCommentsRequestDto) {
        return commentService.loadComments(loadCommentsRequestDto);
    }

    @PostMapping
    public void addComment(AddCommentRequestDto addCommentRequestDto) {
        commentService.addComment(addCommentRequestDto);
    }

    @PatchMapping("/{commentId}")
    public void updateComment(UpdateCommentRequestDto updateCommentRequestDto) {
        commentService.updateComment(updateCommentRequestDto);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(DeleteCommentRequestDto deleteCommentRequestDto) {
        commentService.deleteComment(deleteCommentRequestDto);
    }
}