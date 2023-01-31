package com.atoz.post;

import com.atoz.error.exception.NoRowsFoundException;
import com.atoz.post.dto.domain.Post;
import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.response.OpenPostResponseDto;
import com.atoz.post.helper.SpyPostMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

public class PostServiceTest {

    private PostService sut;
    private SpyPostMapper postMapper;

    @BeforeEach
    void setUp() {
        postMapper = new SpyPostMapper();
        sut = new PostServiceImpl(postMapper);
    }

    @Test
    void openPost_조회된_게시글을_응답_객체로_감싸서_반환한다() {
        long postId = this.addSamplePost().getPostId();


        OpenPostResponseDto result = sut.openPost(postId);


        Post post = result.getPost();
        assertNotNull(post);
        assertEquals(postId, post.getPostId());
    }

    @Test
    void openPost_게시글을_조회할_때마다_조회수가_1_증가한다() {
        long postId = this.addSamplePost().getPostId();


        sut.openPost(postId);
        sut.openPost(postId);
        OpenPostResponseDto result = sut.openPost(postId);


        assertEquals(3, result.getPost().getViewCount());
    }

    @Test
    void openPost_조회할_게시글이_없으면_예외를_던진다() {
        long postId = 999999;


        Throwable thrown = catchThrowable(() -> {
            sut.openPost(postId);
        });


        assertInstanceOf(NoRowsFoundException.class, thrown);
    }

    private Post addSamplePost() {
        AddPostRequestDto addPostRequestDto = AddPostRequestDto.builder()
                .title("sample title")
                .content("sample content")
                .categories("category1,category2,category3")
                .hashtags("hashtag1,hashtag2,hashtag3")
                .build();
        postMapper.addPost(addPostRequestDto, "testUserId");

        return postMapper.cloneAndFlushCapturedPost();
    }

    private void addSamplePosts(int size) {
        for (int i = 0; i < size; i++) {
            this.addSamplePost();
        }
    }
}