package com.atoz.post.helper;

import com.atoz.post.PostMapper;
import com.atoz.post.entity.PostEntity;

public class SpyPostMapper implements PostMapper {

    public PostEntity receivedPostEntity;

    @Override
    public void addPost(PostEntity postEntity) {
        receivedPostEntity = postEntity;
    }

    @Override
    public void updatePost(PostEntity postEntity) {
        receivedPostEntity = postEntity;
    }

    @Override
    public void deletePost(PostEntity postEntity) {
        receivedPostEntity = postEntity;
    }

    @Override
    public PostEntity findById(PostEntity postEntity) {
        receivedPostEntity = postEntity;

        return null;
    }
}
