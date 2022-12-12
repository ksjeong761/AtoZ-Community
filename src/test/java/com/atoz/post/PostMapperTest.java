package com.atoz.post;

import com.atoz.post.dto.PostDto;
import com.atoz.user.UserMapper;
import com.atoz.user.entity.Authority;
import com.atoz.user.entity.UserEntity;
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
    private UserEntity signedUpUser;

    @BeforeEach
    void setUp() {
        // 외래키 제약조건 때문에 회원가입이 되어 있어야 게시글을 조작할 수 있습니다.
        signedUpUser = UserEntity.builder()
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
        LocalDateTime addedTime = LocalDateTime.now();
        PostDto addedPost = PostDto.builder()
                .postId(1)
                .userId(signedUpUser.getUserId())
                .title("testTitle")
                .content("testContent")
                .likeCount(0)
                .viewCount(0)
                .comments("")
                .createdAt(addedTime)
                .updatedAt(addedTime)
                .build();


        sut.addPost(addedPost);


        PostDto foundPost = sut.findById(addedPost);
        assertNotNull(foundPost);
        assertEquals(addedPost.getTitle(), foundPost.getTitle());
        assertEquals(addedPost.getContent(), foundPost.getContent());
    }

    @Test
    void updatePost_게시글이_수정된다() {
        LocalDateTime addedTime = LocalDateTime.now();
        PostDto addedPost = PostDto.builder()
                .userId(signedUpUser.getUserId())
                .title("testTitle")
                .content("testContent")
                .likeCount(0)
                .viewCount(0)
                .comments("")
                .createdAt(addedTime)
                .updatedAt(addedTime)
                .build();
        sut.addPost(addedPost);

        LocalDateTime updatedTime = LocalDateTime.now();
        PostDto updatedPost = PostDto.builder()
                .postId(1)
                .userId(signedUpUser.getUserId())
                .title("newTitle")
                .content("newContent")
                .updatedAt(updatedTime)
                .build();


        sut.updatePost(updatedPost);


        PostDto foundPost = sut.findById(updatedPost);
        assertNotNull(foundPost);
        assertEquals(updatedPost.getTitle(), foundPost.getTitle());
        assertEquals(updatedPost.getContent(), foundPost.getContent());
    }

    @Test
    void deletePost_게시글이_삭제된다() {
        LocalDateTime addedTime = LocalDateTime.now();
        PostDto addedPost = PostDto.builder()
                .userId(signedUpUser.getUserId())
                .title("testTitle")
                .content("testContent")
                .likeCount(0)
                .viewCount(0)
                .comments("")
                .createdAt(addedTime)
                .updatedAt(addedTime)
                .build();
        sut.addPost(addedPost);

        PostDto deletedPost = PostDto.builder()
                .postId(1)
                .userId(signedUpUser.getUserId())
                .build();


        sut.deletePost(deletedPost);


        PostDto foundPost = sut.findById(deletedPost);
        assertNull(foundPost);
    }

    @Test
    void findById_게시글이_조회된다() {
        PostDto post = PostDto.builder()
                .postId(1)
                .userId(signedUpUser.getUserId())
                .build();


        sut.findById(post);
    }
}
