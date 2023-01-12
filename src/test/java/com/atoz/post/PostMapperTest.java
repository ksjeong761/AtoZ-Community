package com.atoz.post;

import com.atoz.post.dto.domain.Post;
import com.atoz.post.dto.domain.PostSummary;
import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.LoadPostsRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.user.Authority;
import com.atoz.user.UserMapper;
import com.atoz.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "/application-test.yaml")
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
    void loadPosts_게시글_목록을_불러온다() {
        this.addPosts(10);
        LoadPostsRequestDto loadPostsRequestDto = LoadPostsRequestDto.builder()
                .offset(0)
                .limit(10)
                .build();


        List<PostSummary> result = sut.loadPosts(loadPostsRequestDto);


        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(loadPostsRequestDto.getLimit(), result.size());
    }

    @Test
    void loadPosts_게시글_수보다_많은_목록을_불러오면_게시글_목록이_모두_조회된다() {
        long listSize = 7;
        this.addPosts(listSize);
        LoadPostsRequestDto loadPostsRequestDto = LoadPostsRequestDto.builder()
                .offset(0)
                .limit(10)
                .build();


        List<PostSummary> result = sut.loadPosts(loadPostsRequestDto);


        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(listSize, result.size());
    }

    @Test
    void addPost_게시글이_저장된다() {
        AddPostRequestDto addPostRequestDto = AddPostRequestDto.builder()
                .title("testTitle")
                .content("testContent")
                .build();


        sut.addPost(addPostRequestDto, signedUpUser.getUserId());


        Optional<Post> result = openPost(addPostRequestDto.getPostId());
        Post post = result.get();
        assertEquals(addPostRequestDto.getTitle(), post.getTitle());
        assertEquals(addPostRequestDto.getContent(), post.getContent());
    }

    @Test
    void addPost_게시글을_추가한_시각이_저장된다() {
        AddPostRequestDto addPostRequestDto = AddPostRequestDto.builder()
                .title("testTitle")
                .content("testContent")
                .build();


        sut.addPost(addPostRequestDto, signedUpUser.getUserId());


        Optional<Post> result = openPost(addPostRequestDto.getPostId());
        Post post = result.get();
        LocalDateTime now = LocalDateTime.now();
        assertEquals(now.getDayOfMonth(), post.getCreatedAt().getDayOfMonth());
        assertEquals(now.getHour(), post.getCreatedAt().getHour());
        assertEquals(now.getMinute(), post.getCreatedAt().getMinute());
    }

    @Test
    void updatePost_게시글이_수정된다() {
        long postId = this.addPost();

        UpdatePostRequestDto updatePostRequestDto = UpdatePostRequestDto.builder()
                .userId(signedUpUser.getUserId())
                .title("newTitle")
                .content("newContent")
                .build();


        sut.updatePost(postId, updatePostRequestDto);


        Optional<Post> result = openPost(postId);
        Post post = result.get();
        assertEquals(updatePostRequestDto.getTitle(), post.getTitle());
        assertEquals(updatePostRequestDto.getContent(), post.getContent());
    }

    @Test
    void updatePost_게시글을_수정한_시각이_저장된다() {
        long postId = this.addPost();

        UpdatePostRequestDto updatePostRequestDto = UpdatePostRequestDto.builder()
                .userId(signedUpUser.getUserId())
                .title("newTitle")
                .content("newContent")
                .build();


        sut.updatePost(postId, updatePostRequestDto);


        Optional<Post> result = openPost(postId);
        Post post = result.get();
        LocalDateTime now = LocalDateTime.now();
        assertEquals(now.getDayOfMonth(), post.getUpdatedAt().getDayOfMonth());
        assertEquals(now.getHour(), post.getUpdatedAt().getHour());
        assertEquals(now.getMinute(), post.getUpdatedAt().getMinute());
    }

    @Test
    void deletePost_게시글이_삭제된다() {
        long postId = this.addPost();

        DeletePostRequestDto deletePostRequestDto = DeletePostRequestDto.builder()
                .userId(signedUpUser.getUserId())
                .build();


        sut.deletePost(postId, deletePostRequestDto);


        Optional<Post> result = openPost(postId);
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_게시글이_조회된다() {
        long postId = this.addPost();


        Optional<Post> result = sut.findPostByPostId(postId);


        assertTrue(result.isPresent());
        assertEquals(postId, result.get().getPostId());
    }

    private void addPosts(long listSize) {
        for (long i = 0; i < listSize; i++) {
            this.addPost();
        }
    }

    private long addPost() {
        AddPostRequestDto addPostRequestDto = AddPostRequestDto.builder()
                .title("testTitle")
                .content("testContent")
                .build();
        sut.addPost(addPostRequestDto, signedUpUser.getUserId());

        return addPostRequestDto.getPostId();
    }

    private Optional<Post> openPost(long postId) {
        return sut.findPostByPostId(postId);
    }
}
