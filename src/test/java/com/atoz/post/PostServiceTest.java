package com.atoz.post;

import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.dto.PostDto;
import com.atoz.post.helper.SpyPostMapper;
import com.atoz.security.authentication.helper.CustomWithMockUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class PostServiceTest {

    private SpyPostMapper postMapper = new SpyPostMapper();

    private PostService sut = new PostServiceImpl(postMapper);
    private static final String USER_ID = "testUserId";

    @Test
    @CustomWithMockUser
    void addPost_게시글을_추가한_사용자_아이디가_저장된다() {
        AddPostRequestDto addPostRequestDto = AddPostRequestDto.builder()
                .title("testTitle")
                .content("testContent")
                .build();


        sut.addPost(addPostRequestDto);


        PostDto receivedPostDto = postMapper.receivedPostDto;
        assertNotNull(receivedPostDto);
        assertEquals(USER_ID, receivedPostDto.getUserId());
    }

    @Test
    @CustomWithMockUser
    void updatePost_게시글을_수정한_사용자_아이디가_저장된다() {
        UpdatePostRequestDto updatePostRequestDto = UpdatePostRequestDto.builder()
                .postId(1)
                .title("testTitle")
                .content("testContent")
                .build();


        sut.updatePost(updatePostRequestDto);


        PostDto receivedPostDto = postMapper.receivedPostDto;
        assertNotNull(receivedPostDto);
        assertEquals(USER_ID, receivedPostDto.getUserId());
    }
}
