package com.atoz.post;

import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.OpenPostRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.dto.response.OpenPostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public void addPost(@Valid @RequestBody AddPostRequestDto addPostRequestDto,
                        @AuthenticationPrincipal UserDetails userDetails) {
        postService.addPost(addPostRequestDto, userDetails);
    }

    @PreAuthorize("hasRole('USER') and principal.username == #updatePostRequestDto.getUserId()")
    @PatchMapping
    public void updatePost(@Valid @RequestBody UpdatePostRequestDto updatePostRequestDto) {
        postService.updatePost(updatePostRequestDto);
    }

    @PreAuthorize("hasRole('USER') and principal.username == #deletePostRequestDto.getUserId()")
    @DeleteMapping
    public void deletePost(@Valid @RequestBody DeletePostRequestDto deletePostRequestDto) {
        postService.deletePost(deletePostRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public OpenPostResponseDto openPost(@Valid @RequestBody OpenPostRequestDto openPostRequestDto) {
        return postService.openPost(openPostRequestDto);
    }
}
