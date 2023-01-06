package com.atoz.post.helper;

import com.atoz.post.PostMapper;
import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.OpenPostRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.dto.response.OpenPostResponseDto;

import java.util.Optional;

public class SpyPostMapper implements PostMapper {

    public String receivedUserId;

    @Override
    public void addPost(AddPostRequestDto addPostRequestDto, String userId) {
        receivedUserId = userId;
    }

    @Override
    public void updatePost(UpdatePostRequestDto updatePostRequestDto) {

    }

    @Override
    public void deletePost(DeletePostRequestDto deletePostRequestDto) {

    }

    @Override
    public Optional<OpenPostResponseDto> findPostByPostId(long postId) {
        return null;
    }
}
