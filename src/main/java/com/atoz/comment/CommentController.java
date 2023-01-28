package com.atoz.comment;

import com.atoz.comment.dto.request.AddCommentRequestDto;
import com.atoz.comment.dto.request.DeleteCommentRequestDto;
import com.atoz.comment.dto.request.LoadCommentsRequestDto;
import com.atoz.comment.dto.request.UpdateCommentRequestDto;
import com.atoz.comment.dto.response.LoadCommentsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public LoadCommentsResponseDto loadComments(LoadCommentsRequestDto loadCommentsRequestDto) {
        return commentService.loadComments(loadCommentsRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public void addComment(@Valid @RequestBody AddCommentRequestDto addCommentRequestDto,
                           @AuthenticationPrincipal UserDetails userDetails) {
        commentService.addComment(addCommentRequestDto, userDetails.getUsername());
    }

    @PreAuthorize("hasRole('USER') and principal.username == #updateCommentRequestDto.getUserId()")
    @PatchMapping("/{commentId}")
    public void updateComment(@PathVariable @Min(1) long commentId,
                              @Valid @RequestBody UpdateCommentRequestDto updateCommentRequestDto) {
        commentService.updateComment(commentId, updateCommentRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{commentId}/like_count")
    public void increaseLikeCount(@PathVariable @Min(1) long commentId) {
        commentService.increaseLikeCount(commentId);
    }

    @PreAuthorize("hasRole('USER') and principal.username == #deleteCommentRequestDto.getUserId()")
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable @Min(1) long commentId,
                              @Valid @RequestBody DeleteCommentRequestDto deleteCommentRequestDto) {
        commentService.deleteComment(commentId, deleteCommentRequestDto);
    }
}