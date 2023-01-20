package com.atoz.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    void loadComments() {
        commentService.loadComments();
    }

    @PostMapping
    void addComment() {
        commentService.addComment();
    }

    @PatchMapping("/{commentId}")
    void updateComment() {
        commentService.updateComment();
    }

    @DeleteMapping("/{commentId}")
    void deleteComment() {
        commentService.deleteComment();
    }
}