package com.atoz.post.helper;

import com.atoz.post.PostService;
import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.LoadPostSummariesRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.dto.response.LoadPostSummariesResponseDto;
import com.atoz.post.dto.response.OpenPostResponseDto;

public class SpyPostService implements PostService {

    public LoadPostSummariesRequestDto capturedLoadPostSummariesRequestDto = null;
    public String capturedUserId = null;

    @Override
    public LoadPostSummariesResponseDto loadPostSummaries(LoadPostSummariesRequestDto loadPostSummariesRequestDto) {
        this.capturedLoadPostSummariesRequestDto = loadPostSummariesRequestDto;
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