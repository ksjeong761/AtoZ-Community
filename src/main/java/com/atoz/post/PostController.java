package com.atoz.post;

import com.atoz.post.entity.PostEntity;
import com.atoz.post.dto.AddPostDTO;
import com.atoz.post.dto.DeletePostDTO;
import com.atoz.post.dto.OpenPostDTO;
import com.atoz.post.dto.UpdatePostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PutMapping
    public void addPost(@Validated @RequestBody AddPostDTO addPostDTO) {
        postService.addPost(addPostDTO);
    }

    @PatchMapping
    public void updatePost(@Validated @RequestBody UpdatePostDTO updatePostDTO) {
        postService.updatePost(updatePostDTO);
    }

    @DeleteMapping
    public void deletePost(@Validated @RequestBody DeletePostDTO deletePostDTO) {
        postService.deletePost(deletePostDTO);
    }

    @GetMapping
    public PostEntity openPost(@Validated @RequestBody OpenPostDTO openPostDTO) {
        return postService.findById(openPostDTO);
    }
}
