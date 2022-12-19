package com.atoz.post;

import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.OpenPostRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.dto.response.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public void addPost(@Validated @RequestBody AddPostRequestDto addPostRequestDto) {
        postService.addPost(addPostRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping
    public void updatePost(@Validated @RequestBody UpdatePostRequestDto updatePostRequestDto) {
        postService.updatePost(updatePostRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping
    public void deletePost(@Validated @RequestBody DeletePostRequestDto deletePostRequestDto) {
        postService.deletePost(deletePostRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public PostResponseDto openPost(@Validated @RequestBody OpenPostRequestDto openPostRequestDto) {
        return postService.findById(openPostRequestDto);
    }
}
