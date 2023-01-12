package com.atoz.post.helper;

import com.atoz.post.PostMapper;
import com.atoz.post.dto.domain.Post;
import com.atoz.post.dto.domain.PostSummary;
import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.LoadPostsRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;

import java.util.List;
import java.util.Optional;

public class SpyPostMapper implements PostMapper {

    public String receivedUserId;

    @Override
    public void addPost(AddPostRequestDto addPostRequestDto, String userId) {
        receivedUserId = userId;
    }

    @Override
    public void updatePost(long postId, UpdatePostRequestDto updatePostRequestDto) {

    }

    @Override
    public void deletePost(long postId, DeletePostRequestDto deletePostRequestDto) {

    }

    @Override
    public Optional<Post> findPostByPostId(long postId) {
        return null;
    }

    @Override
    public List<PostSummary> loadPosts(LoadPostsRequestDto loadPostsRequestDto) {
        return null;
    }
}
