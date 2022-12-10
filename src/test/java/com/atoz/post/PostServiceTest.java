package com.atoz.post;

import com.atoz.post.dto.AddPostDTO;
import com.atoz.post.dto.UpdatePostDTO;
import com.atoz.post.entity.PostEntity;
import com.atoz.post.helper.SpyPostMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PostServiceTest {

    private SpyPostMapper postMapper = new SpyPostMapper();

    private PostService sut = new PostServiceImpl(postMapper);
    private String userId = "testUserId";

    @BeforeEach
    void setUp() {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_FAKE"));
        UserDetails principal = User.builder()
                .username(userId)
                .password("")
                .authorities(authorities)
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void addPost_게시글을_추가한_사용자_아이디가_저장된다() {
        AddPostDTO addPostDTO = AddPostDTO.builder()
                .title("testTitle")
                .content("testContent")
                .build();


        sut.addPost(addPostDTO);


        PostEntity receivedPostEntity = postMapper.receivedPostEntity;
        assertNotNull(receivedPostEntity);
        assertEquals(userId, receivedPostEntity.getUserId());
    }

    @Test
    void addPost_게시글을_추가한_시간이_저장된다() {
        LocalDateTime now = LocalDateTime.now();
        AddPostDTO addPostDTO = AddPostDTO.builder()
                .title("testTitle")
                .content("testContent")
                .build();


        sut.addPost(addPostDTO);


        PostEntity receivedPostEntity = postMapper.receivedPostEntity;
        assertNotNull(receivedPostEntity);
        assertTrue(now.isEqual(receivedPostEntity.getCreatedAt()));
    }


    @Test
    void updatePost_게시글을_수정한_사용자_아이디가_저장된다() {
        UpdatePostDTO updatePostDTO = UpdatePostDTO.builder()
                .postId(1)
                .title("testTitle")
                .content("testContent")
                .build();


        sut.updatePost(updatePostDTO);


        PostEntity receivedPostEntity = postMapper.receivedPostEntity;
        assertNotNull(receivedPostEntity);
        assertEquals(userId, receivedPostEntity.getUserId());
    }

    @Test
    void updatePost_게시글을_수정한_시간이_저장된다() {
        LocalDateTime now = LocalDateTime.now();
        UpdatePostDTO updatePostDTO = UpdatePostDTO.builder()
                .postId(1)
                .title("testTitle")
                .content("testContent")
                .build();


        sut.updatePost(updatePostDTO);


        PostEntity receivedPostEntity = postMapper.receivedPostEntity;
        assertNotNull(receivedPostEntity);
        assertTrue(now.isEqual(receivedPostEntity.getUpdatedAt()));
    }
}
