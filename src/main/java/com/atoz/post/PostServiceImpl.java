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

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;

    @Override
    public void addPost(AddPostRequestDto addPostRequestDto) {
        String userId = loadUserIdFromContext();
        LocalDateTime now = LocalDateTime.now();

        PostDto post = PostDto.builder()
                .userId(userId)
                .title(addPostRequestDto.getTitle())
                .content(addPostRequestDto.getContent())
                .likeCount(0)
                .viewCount(0)
                .comments("")
                .createdAt(now)
                .updatedAt(now)
                .build();

        postMapper.addPost(post);
    }

    @Override
    public void updatePost(UpdatePostRequestDto updatePostRequestDto) {
        String userId = loadUserIdFromContext();
        LocalDateTime now = LocalDateTime.now();

        PostDto post = PostDto.builder()
                .postId(updatePostRequestDto.getPostId())
                .userId(userId)
                .title(updatePostRequestDto.getTitle())
                .content(updatePostRequestDto.getContent())
                .updatedAt(now)
                .build();

        postMapper.updatePost(post);
    }

    @Override
    public void deletePost(DeletePostRequestDto deletePostRequestDto) {
        String userId = loadUserIdFromContext();

        PostDto post = PostDto.builder()
                .postId(deletePostRequestDto.getPostId())
                .userId(userId)
                .build();

        postMapper.deletePost(post);
    }

    @Override
    public PostResponseDto findById(OpenPostRequestDto openPostRequestDto) {
        String userId = loadUserIdFromContext();

        PostDto post = PostDto.builder()
                .postId(openPostRequestDto.getPostId())
                .userId(userId)
                .build();

        return postMapper.findById(post).toPostResponseDto();
    }

    private String loadUserIdFromContext() {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
