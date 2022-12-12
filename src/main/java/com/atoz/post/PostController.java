package com.atoz.post;

import com.atoz.post.dto.PostDto;
import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.OpenPostRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PutMapping
    public void addPost(@Validated @RequestBody AddPostRequestDto addPostRequestDto) {
        postService.addPost(addPostRequestDto);
    }

    @PatchMapping
    public void updatePost(@Validated @RequestBody UpdatePostRequestDto updatePostRequestDto) {
        postService.updatePost(updatePostRequestDto);
    }

    @DeleteMapping
    public void deletePost(@Validated @RequestBody DeletePostRequestDto deletePostRequestDto) {
        postService.deletePost(deletePostRequestDto);
    }

    @GetMapping
    public PostDto openPost(@Validated @RequestBody OpenPostRequestDto openPostRequestDto) {
        return postService.findById(openPostRequestDto);
    }
}
