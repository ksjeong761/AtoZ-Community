package com.atoz.comment;

import com.atoz.comment.dto.request.AddCommentRequestDto;
import com.atoz.comment.dto.request.DeleteCommentRequestDto;
import com.atoz.comment.dto.request.LoadCommentsRequestDto;
import com.atoz.comment.dto.request.UpdateCommentRequestDto;
import com.atoz.comment.dto.response.LoadCommentsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

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
    public void addComment(@Valid @RequestBody AddCommentRequestDto addCommentRequestDto) {
        commentService.addComment(addCommentRequestDto);
    }

    @PatchMapping("/{commentId}")
    public void updateComment(@PathVariable @Min(1) long commentId,
                              @Valid @RequestBody UpdateCommentRequestDto updateCommentRequestDto) {
        commentService.updateComment(commentId, updateCommentRequestDto);
    }

    @PatchMapping("/{commentId}/like_count")
    public void increaseLikeCount(@PathVariable @Min(1) long commentId) {
        commentService.increaseLikeCount(commentId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable @Min(1) long commentId,
                              @Valid @RequestBody DeleteCommentRequestDto deleteCommentRequestDto) {
        commentService.deleteComment(commentId, deleteCommentRequestDto);
    }
}