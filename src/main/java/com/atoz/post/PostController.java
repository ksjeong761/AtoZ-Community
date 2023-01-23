package com.atoz.post;

import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.LoadPostsRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.dto.response.LoadPostsResponseDto;
import com.atoz.post.dto.response.OpenPostResponseDto;
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
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public void addPost(@Valid @RequestBody AddPostRequestDto addPostRequestDto,
                        @AuthenticationPrincipal UserDetails userDetails) {
        postService.addPost(addPostRequestDto, userDetails);
    }

    @PreAuthorize("hasRole('USER') and principal.username == #updatePostRequestDto.getUserId()")
    @PatchMapping("/{postId}")
    public void updatePost(@PathVariable @Min(1) long postId,
                           @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto) {
        postService.updatePost(postId, updatePostRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{postId}/like_count")
    public void increaseLikeCount(@PathVariable @Min(1) long postId) {
        postService.increaseLikeCount(postId);
    }

    @PreAuthorize("hasRole('USER') and principal.username == #deletePostRequestDto.getUserId()")
    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable @Min(1) long postId,
                           @Valid @RequestBody DeletePostRequestDto deletePostRequestDto) {
        postService.deletePost(postId, deletePostRequestDto);
    }

    @GetMapping("/{postId}")
    public OpenPostResponseDto openPost(@PathVariable @Min(1) long postId) {
        return postService.openPost(postId);
    }

    @GetMapping
    public LoadPostsResponseDto loadPosts(@Valid @ModelAttribute LoadPostsRequestDto loadPostsRequestDto) {
        return postService.loadPosts(loadPostsRequestDto);
    }
}
