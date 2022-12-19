package com.atoz.post;

import com.atoz.post.dto.PostDto;
import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.OpenPostRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.dto.response.PostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;

    @Override
    public void addPost(AddPostRequestDto addPostRequestDto) {
        PostDto post = PostDto.builder()
                .userId(loadUserIdFromContext())
                .title(addPostRequestDto.getTitle())
                .content(addPostRequestDto.getContent())
                .build();

        postMapper.addPost(post);
    }

    @Override
    public void updatePost(UpdatePostRequestDto updatePostRequestDto) {
        PostDto post = PostDto.builder()
                .postId(updatePostRequestDto.getPostId())
                .userId(updatePostRequestDto.getUserId())
                .title(updatePostRequestDto.getTitle())
                .content(updatePostRequestDto.getContent())
                .build();

        postMapper.updatePost(post);
    }

    @Override
    public void deletePost(DeletePostRequestDto deletePostRequestDto) {
        PostDto post = PostDto.builder()
                .postId(deletePostRequestDto.getPostId())
                .userId(deletePostRequestDto.getUserId())
                .build();

        postMapper.deletePost(post);
    }

    @Override
    public PostResponseDto findById(OpenPostRequestDto openPostRequestDto) {
        PostDto post = PostDto.builder()
                .postId(openPostRequestDto.getPostId())
                .build();

        return postMapper.findById(post).toPostResponseDto();
    }

    private String loadUserIdFromContext() {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
