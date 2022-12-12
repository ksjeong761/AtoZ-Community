package com.atoz.security.token;

import com.atoz.security.token.dto.RefreshTokenDto;
import com.atoz.user.UserMapper;
import com.atoz.user.Authority;
import com.atoz.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "/application-test.yaml")
@MybatisTest
class RefreshTokenMapperTest {

    @Autowired
    private RefreshTokenMapper sut;

    @Autowired
    private UserMapper userMapper;

    private UserDto signedUpUser;

    @BeforeEach
    void setUp() {
        // 외래키 제약조건 때문에 회원가입이 되어 있어야 토큰을 조작할 수 있습니다.
        signedUpUser = UserDto.builder()
                .userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        userMapper.addUser(signedUpUser);
        userMapper.addAuthority(signedUpUser);

        RefreshTokenDto savedToken = RefreshTokenDto.builder()
                .tokenKey(signedUpUser.getUserId())
                .tokenValue("testRefreshToken")
                .build();
        sut.saveToken(savedToken);
    }

    @Test
    void saveToken_회원가입_되어있다면_리프레시토큰을_저장할_수_있다() {
        UserDto newUser = UserDto.builder()
                .userId("newUserId")
                .password("newPassword")
                .nickname("newNickname")
                .email("newTest@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        userMapper.addUser(newUser);
        userMapper.addAuthority(newUser);

        RefreshTokenDto refreshTokenDto = RefreshTokenDto.builder()
                .tokenKey(newUser.getUserId())
                .tokenValue("newRefreshToken")
                .build();


        sut.saveToken(refreshTokenDto);


        Optional<RefreshTokenDto> foundRefreshToken = sut.findTokenByKey(refreshTokenDto.getTokenKey());
        assertTrue(foundRefreshToken.isPresent());
        assertEquals(foundRefreshToken.get().getTokenKey(), refreshTokenDto.getTokenKey());
        assertEquals(foundRefreshToken.get().getTokenValue(), refreshTokenDto.getTokenValue());
    }

    @Test
    void saveToken_회원가입_되어있지_않다면_리프레시토큰을_저장할_때_예외가_발생한다() {
        RefreshTokenDto refreshTokenDto = RefreshTokenDto.builder()
                .tokenKey("newUserId")
                .tokenValue("newRefreshToken")
                .build();


        Throwable thrown = catchThrowable(() -> {
            sut.saveToken(refreshTokenDto);
        });


        assertInstanceOf(DataIntegrityViolationException.class, thrown);
    }

    @Test
    void findByKey_리프레시토큰이_없으면_토큰을_찾을_때_null을_반환한다() {
        String wrongUserId = "wrongUserId";


        Optional<RefreshTokenDto> foundRefreshToken = sut.findTokenByKey(wrongUserId);


        assertTrue(foundRefreshToken.isEmpty());
    }

    @Test
    void updateToken_저장된_리프레시토큰을_업데이트_할수있다() {
        RefreshTokenDto updateRequest = RefreshTokenDto.builder()
                .tokenKey(signedUpUser.getUserId())
                .tokenValue("updateRefreshToken")
                .build();


        sut.updateToken(updateRequest);


        Optional<RefreshTokenDto> updatedTokenDto = sut.findTokenByKey(signedUpUser.getUserId());
        assertTrue(updatedTokenDto.isPresent());
        assertEquals(updatedTokenDto.get().getTokenKey(), updateRequest.getTokenKey());
        assertEquals(updatedTokenDto.get().getTokenValue(), updateRequest.getTokenValue());
    }

    @Test
    void deleteToken_저장된_리프레시토큰을_삭제할수있다() {
        String targetUserId = signedUpUser.getUserId();


        sut.deleteToken(targetUserId);


        Optional<RefreshTokenDto> deletedTokenDto = sut.findTokenByKey(signedUpUser.getUserId());
        assertTrue(deletedTokenDto.isEmpty());
    }
}