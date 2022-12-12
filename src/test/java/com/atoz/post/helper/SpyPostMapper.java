package com.atoz.post.helper;

import com.atoz.post.PostMapper;
import com.atoz.post.dto.PostDto;

public class SpyPostMapper implements PostMapper {

    public PostDto receivedPostDto;

    @Override
    public void addPost(PostDto postDto) {
        receivedPostDto = postDto;
    }

    @Override
    public void updatePost(PostDto postDto) {
        receivedPostDto = postDto;
    }

    @Override
    public void deletePost(PostDto postDto) {
        receivedPostDto = postDto;
    }

    @Override
    public PostDto findById(PostDto postDto) {
        receivedPostDto = postDto;

        return null;
    }
}
