package com.atoz.post.helper;

import com.atoz.post.PostService;
import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.LoadPostsRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.dto.response.LoadPostsResponseDto;
import com.atoz.post.dto.response.OpenPostResponseDto;

public class SpyPostService implements PostService {

    public LoadPostsRequestDto capturedLoadPostsRequestDto = null;
    public String capturedUserId = null;

    @Override
    public LoadPostsResponseDto loadPosts(LoadPostsRequestDto loadPostsRequestDto) {
        this.capturedLoadPostsRequestDto = loadPostsRequestDto;
        return null;
    }

    @Override
    public void addPost(AddPostRequestDto addPostRequestDto, String userId) {
        this.capturedUserId = userId;
    }

    @Override
    public OpenPostResponseDto openPost(long postId) {
        return null;
    }

    @Override
    public void updatePost(long postId, UpdatePostRequestDto updatePostRequestDto) {

    }

    @Override
    public void increaseLikeCount(long postId) {

    }

    @Override
    public void deletePost(long postId, DeletePostRequestDto deletePostRequestDto) {

    }
}