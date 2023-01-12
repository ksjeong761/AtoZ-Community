package com.atoz.post;

import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.LoadPostsRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.dto.response.LoadPostsResponseDto;
import com.atoz.post.dto.response.OpenPostResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface PostService {

    void addPost(AddPostRequestDto addPostRequestDto, UserDetails userDetails);

    void updatePost(long postId, UpdatePostRequestDto updatePostRequestDto);

    void increaseLikeCount(long postId);

    void deletePost(long postId, DeletePostRequestDto deletePostRequestDto);

    OpenPostResponseDto openPost(long postId);

    LoadPostsResponseDto loadPosts(LoadPostsRequestDto loadPostsRequestDto);
}
