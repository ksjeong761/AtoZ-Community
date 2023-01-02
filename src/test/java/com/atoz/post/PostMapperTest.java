package com.atoz.post;

import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.OpenPostRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.dto.response.OpenPostResponseDto;
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
import java.util.Optional;
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
        AddPostRequestDto addPostRequestDto = AddPostRequestDto.builder()
                .title("testTitle")
                .content("testContent")
                .build();


        sut.addPost(addPostRequestDto, signedUpUser.getUserId());


        Optional<OpenPostResponseDto> result = openPost(1);
        assertTrue(result.isPresent());
        assertEquals(addPostRequestDto.getTitle(), result.get().getTitle());
        assertEquals(addPostRequestDto.getContent(), result.get().getContent());
    }

    @Test
    void addPost_게시글을_추가한_시각이_저장된다() {
        AddPostRequestDto addPostRequestDto = AddPostRequestDto.builder()
                .title("testTitle")
                .content("testContent")
                .build();


        sut.addPost(addPostRequestDto, signedUpUser.getUserId());


        Optional<OpenPostResponseDto> result = openPost(1);
        LocalDateTime now = LocalDateTime.now();
        assertEquals(now.getDayOfMonth(), result.get().getCreatedAt().getDayOfMonth());
        assertEquals(now.getHour(), result.get().getCreatedAt().getHour());
        assertEquals(now.getMinute(), result.get().getCreatedAt().getMinute());
    }

    @Test
    void updatePost_게시글이_수정된다() {
        addPost();

        UpdatePostRequestDto updatePostRequestDto = UpdatePostRequestDto.builder()
                .postId(1)
                .userId(signedUpUser.getUserId())
                .title("newTitle")
                .content("newContent")
                .build();


        sut.updatePost(updatePostRequestDto);


        Optional<OpenPostResponseDto> result = openPost(updatePostRequestDto.getPostId());
        assertEquals(updatePostRequestDto.getTitle(), result.get().getTitle());
        assertEquals(updatePostRequestDto.getContent(), result.get().getContent());
    }

    @Test
    void updatePost_게시글을_수정한_시각이_저장된다() {
        addPost();

        UpdatePostRequestDto updatePostRequestDto = UpdatePostRequestDto.builder()
                .postId(1)
                .userId(signedUpUser.getUserId())
                .title("newTitle")
                .content("newContent")
                .build();


        sut.updatePost(updatePostRequestDto);


        Optional<OpenPostResponseDto> result = openPost(updatePostRequestDto.getPostId());
        LocalDateTime now = LocalDateTime.now();
        assertEquals(now.getDayOfMonth(), result.get().getUpdatedAt().getDayOfMonth());
        assertEquals(now.getHour(), result.get().getUpdatedAt().getHour());
        assertEquals(now.getMinute(), result.get().getUpdatedAt().getMinute());
    }

    @Test
    void deletePost_게시글이_삭제된다() {
        addPost();

        DeletePostRequestDto deletePostRequestDto = DeletePostRequestDto.builder()
                .postId(1)
                .userId(signedUpUser.getUserId())
                .build();


        sut.deletePost(deletePostRequestDto);


        Optional<OpenPostResponseDto> result = openPost(deletePostRequestDto.getPostId());
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_게시글이_조회된다() {
        addPost();

        OpenPostRequestDto openPostRequestDto = OpenPostRequestDto.builder()
                .postId(1)
                .build();


        Optional<OpenPostResponseDto> result = sut.findPostByPostId(openPostRequestDto.getPostId());


        assertTrue(result.isPresent());
        assertEquals(openPostRequestDto.getPostId(), result.get().getPostId());
    }

    private void addPost() {
        AddPostRequestDto addPostRequestDto = AddPostRequestDto.builder()
                .title("testTitle")
                .content("testContent")
                .build();
        sut.addPost(addPostRequestDto, signedUpUser.getUserId());
    }

    private Optional<OpenPostResponseDto> openPost(int postId) {
        OpenPostRequestDto openPostRequestDto = OpenPostRequestDto.builder()
                .postId(postId)
                .build();
        return sut.findPostByPostId(openPostRequestDto.getPostId());
    }
}
