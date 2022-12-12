package com.atoz.post;

import com.atoz.post.dto.PostDto;
import com.atoz.user.Authority;
import com.atoz.user.UserMapper;
import com.atoz.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "/application-test.yaml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@MybatisTest
public class PostMapperTest {

    @Autowired
    private PostMapper sut;

    @Autowired
    private UserMapper userMapper;
    private UserDto signedUpUser;

    @BeforeEach
    void setUp() {
        // 외래키 제약조건 때문에 회원가입이 되어 있어야 게시글을 조작할 수 있습니다.
        signedUpUser = UserDto.builder()
                .userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        userMapper.addUser(signedUpUser);
        userMapper.addAuthority(signedUpUser);
    }

    @Test
    void addPost_게시글이_저장된다() {
        PostDto addPostDto = PostDto.builder()
                .postId(1)
                .userId(signedUpUser.getUserId())
                .title("testTitle")
                .content("testContent")
                .build();


        sut.addPost(addPostDto);


        PostDto addResult = sut.findById(addPostDto);
        assertNotNull(addResult);
        assertEquals(addPostDto.getTitle(), addResult.getTitle());
        assertEquals(addPostDto.getContent(), addResult.getContent());
    }

    @Test
    void addPost_게시글을_추가한_시각이_저장된다() {
        PostDto addPostDto = PostDto.builder()
                .postId(1)
                .userId(signedUpUser.getUserId())
                .title("testTitle")
                .content("testContent")
                .build();


        sut.addPost(addPostDto);


        PostDto addResult = sut.findById(addPostDto);
        LocalDateTime now = LocalDateTime.now();
        assertEquals(now.getDayOfMonth(), addResult.getCreatedAt().getDayOfMonth());
        assertEquals(now.getHour(), addResult.getCreatedAt().getHour());
        assertEquals(now.getMinute(), addResult.getCreatedAt().getMinute());
    }

    @Test
    void updatePost_게시글이_수정된다() {
        addPost();

        PostDto updatePostDto = PostDto.builder()
                .postId(1)
                .userId(signedUpUser.getUserId())
                .title("newTitle")
                .content("newContent")
                .build();


        sut.updatePost(updatePostDto);


        PostDto updateResult = sut.findById(updatePostDto);
        assertNotNull(updateResult);
        assertEquals(updatePostDto.getTitle(), updateResult.getTitle());
        assertEquals(updatePostDto.getContent(), updateResult.getContent());
    }

    @Test
    void updatePost_게시글을_수정한_시각이_저장된다() {
        addPost();

        PostDto updatePostDto = PostDto.builder()
                .postId(1)
                .userId(signedUpUser.getUserId())
                .title("newTitle")
                .content("newContent")
                .build();


        sut.updatePost(updatePostDto);


        PostDto updateResult = sut.findById(updatePostDto);
        LocalDateTime now = LocalDateTime.now();
        assertEquals(now.getDayOfMonth(), updateResult.getUpdatedAt().getDayOfMonth());
        assertEquals(now.getHour(), updateResult.getUpdatedAt().getHour());
        assertEquals(now.getMinute(), updateResult.getUpdatedAt().getMinute());
    }

    @Test
    void deletePost_게시글이_삭제된다() {
        addPost();

        PostDto deletePostDto = PostDto.builder()
                .postId(1)
                .userId(signedUpUser.getUserId())
                .build();


        sut.deletePost(deletePostDto);


        PostDto deleteResult = sut.findById(deletePostDto);
        assertNull(deleteResult);
    }

    @Test
    void findById_게시글이_조회된다() {
        addPost();

        PostDto findPostDto = PostDto.builder()
                .postId(1)
                .userId(signedUpUser.getUserId())
                .build();


        PostDto findResult = sut.findById(findPostDto);


        assertNotNull(findResult);
        assertEquals(findPostDto.getPostId(), findResult.getPostId());
        assertEquals(findPostDto.getUserId(), findResult.getUserId());
    }

    private void addPost() {
        PostDto addPostDto = PostDto.builder()
                .userId(signedUpUser.getUserId())
                .title("testTitle")
                .content("testContent")
                .build();

        sut.addPost(addPostDto);
    }
}
