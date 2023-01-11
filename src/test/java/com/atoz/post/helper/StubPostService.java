package com.atoz.post.helper;

import com.atoz.post.PostService;
import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.LoadPostsRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.dto.response.LoadPostsResponseDto;
import com.atoz.post.dto.response.OpenPostResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

public class StubPostService implements PostService {
    @Override
    public void addPost(AddPostRequestDto addPostRequestDto, UserDetails userDetails) {

    }

    @Override
    public void updatePost(long postId, UpdatePostRequestDto updatePostRequestDto) {

    }

    @Override
    public void deletePost(long postId, DeletePostRequestDto deletePostRequestDto) {

    }

    @Override
    public OpenPostResponseDto openPost(long postId) {
        return null;
    }

    @Override
    public LoadPostsResponseDto loadPosts(LoadPostsRequestDto loadPostsRequestDto) {
        return null;
    }
}
