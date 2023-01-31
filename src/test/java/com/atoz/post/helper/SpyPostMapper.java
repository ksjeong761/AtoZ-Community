package com.atoz.post.helper;

import com.atoz.post.PostMapper;
import com.atoz.post.dto.domain.Post;
import com.atoz.post.dto.domain.PostSummary;
import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.LoadPostSummariesRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SpyPostMapper implements PostMapper {

    private Map<Long, Post> storage = new HashMap<>();
    private long id = 1L;
    private Post capturedPost = null;

    @Override
    public List<PostSummary> loadPostSummaries(LoadPostSummariesRequestDto loadPostSummariesRequestDto) {
        return storage.values()
                .stream()
                .map((post) -> PostSummary.builder()
                        .postId(post.getPostId())
                        .userId(post.getUserId())
                        .writerAge(33)
                        .title(post.getTitle())
                        .categories(post.getCategories())
                        .hashtags(post.getHashtags())
                        .viewCount(post.getViewCount())
                        .likeCount(post.getLikeCount())
                        .createdAt(post.getCreatedAt())
                        .build())
                .toList();
    }

    @Override
    public int addPost(AddPostRequestDto addPostRequestDto, String userId) {
        Post postWithPostId = Post.builder()
                .postId(id)
                .userId(userId)
                .title(addPostRequestDto.getTitle())
                .content(addPostRequestDto.getContent())
                .categories(addPostRequestDto.getCategories())
                .hashtags(addPostRequestDto.getHashtags())
                .viewCount(0)
                .likeCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        storage.put(id++, postWithPostId);

        capturedPost = postWithPostId;

        int affectedRows = 1;
        return affectedRows;
    }

    @Override
    public Optional<Post> findPostByPostId(long postId) {
        return Optional.ofNullable(storage.get(postId));
    }

    @Override
    public int updatePost(long postId, UpdatePostRequestDto updatePostRequestDto) {
        int affectedRows = 0;
        if (!storage.containsKey(postId)) {
            return affectedRows;
        }

        Post oldPost = storage.get(postId);
        Post newPost = Post.builder()
                .postId(postId)
                .userId(oldPost.getUserId())
                .writerAge(oldPost.getWriterAge())
                .title(updatePostRequestDto.getTitle())
                .content(updatePostRequestDto.getContent())
                .categories(updatePostRequestDto.getCategories())
                .hashtags(updatePostRequestDto.getHashtags())
                .viewCount(oldPost.getViewCount())
                .likeCount(oldPost.getLikeCount())
                .createdAt(oldPost.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
        storage.put(postId, newPost);

        affectedRows = 1;
        return affectedRows;
    }

    @Override
    public int increaseViewCount(long postId) {
        int affectedRows = 0;
        if (!storage.containsKey(postId)) {
            return affectedRows;
        }

        Post oldPost = storage.get(postId);
        Post newPost = Post.builder()
                .postId(postId)
                .userId(oldPost.getUserId())
                .writerAge(oldPost.getWriterAge())
                .title(oldPost.getTitle())
                .content(oldPost.getContent())
                .categories(oldPost.getCategories())
                .hashtags(oldPost.getHashtags())
                .viewCount(oldPost.getViewCount() + 1)
                .likeCount(oldPost.getLikeCount())
                .createdAt(oldPost.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
        storage.put(postId, newPost);

        affectedRows = 1;
        return affectedRows;
    }

    @Override
    public int increaseLikeCount(long postId) {
        int affectedRows = 0;
        if (!storage.containsKey(postId)) {
            return affectedRows;
        }

        Post oldPost = storage.get(postId);
        Post newPost = Post.builder()
                .postId(postId)
                .userId(oldPost.getUserId())
                .writerAge(oldPost.getWriterAge())
                .title(oldPost.getTitle())
                .content(oldPost.getContent())
                .categories(oldPost.getCategories())
                .hashtags(oldPost.getHashtags())
                .viewCount(oldPost.getViewCount())
                .likeCount(oldPost.getLikeCount() + 1)
                .createdAt(oldPost.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        storage.put(postId, newPost);

        affectedRows = 1;
        return affectedRows;
    }

    @Override
    public int deletePost(long postId, DeletePostRequestDto deletePostRequestDto) {
        int affectedRows = 0;
        if (!storage.containsKey(postId)) {
            return affectedRows;
        }

        storage.remove(postId);

        affectedRows = 1;
        return affectedRows;
    }

    public Post cloneAndFlushCapturedPost() {
        Post post = Post.builder()
                .postId(capturedPost.getPostId())
                .userId(capturedPost.getUserId())
                .writerAge(capturedPost.getWriterAge())
                .title(capturedPost.getTitle())
                .content(capturedPost.getContent())
                .categories(capturedPost.getCategories())
                .hashtags(capturedPost.getHashtags())
                .viewCount(capturedPost.getViewCount())
                .likeCount(capturedPost.getLikeCount())
                .createdAt(capturedPost.getCreatedAt())
                .updatedAt(capturedPost.getUpdatedAt())
                .build();

        capturedPost = null;

        return post;
    }
}
