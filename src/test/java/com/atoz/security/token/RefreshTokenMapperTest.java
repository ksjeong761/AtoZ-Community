package com.atoz.security.token;

import com.atoz.user.UserMapper;
import com.atoz.user.entity.Authority;
import com.atoz.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "/application-test.yaml")
@MybatisTest
class RefreshTokenMapperTest {

    @Autowired
    private RefreshTokenMapper sut;

    @Autowired
    private UserMapper userMapper;

    private UserEntity signedUpUser;

    private RefreshTokenEntity savedToken;

    @BeforeEach
    void setUp() {
        // 외래키 제약조건 때문에 회원가입이 되어 있어야 토큰을 조작할 수 있습니다.
        signedUpUser = UserEntity.builder()
                .userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        userMapper.addUser(signedUpUser);
        userMapper.addAuthority(signedUpUser);


        savedToken = RefreshTokenEntity.builder()
                .tokenKey(signedUpUser.getUserId())
                .tokenValue("testRefreshToken")
                .build();
        sut.saveToken(savedToken);
    }

    @Test
    void saveToken_리프레시토큰을_저장할수있다() {
        UserEntity newUser = UserEntity.builder()
                .userId("newUserId")
                .password("newPassword")
                .nickname("newNickname")
                .email("newTest@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        userMapper.addUser(newUser);
        userMapper.addAuthority(newUser);

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .tokenKey(newUser.getUserId())
                .tokenValue("newRefreshToken")
                .build();


        sut.saveToken(refreshTokenEntity);
        Optional<RefreshTokenEntity> foundRefreshToken = sut.findTokenByKey(refreshTokenEntity.getTokenKey());


        assertThat(foundRefreshToken.isPresent()).isTrue();
        assertThat(foundRefreshToken.get().getTokenKey()).isEqualTo(refreshTokenEntity.getTokenKey());
        assertThat(foundRefreshToken.get().getTokenValue()).isEqualTo(refreshTokenEntity.getTokenValue());
    }

    @Test
    void findByKey_리프레시토큰이_없는경우_null을_반환한다() {
        String wrongUserId = "wrongUserId";


        Optional<RefreshTokenEntity> foundRefreshToken = sut.findTokenByKey(wrongUserId);


        assertThat(foundRefreshToken.isEmpty()).isTrue();
    }

    @Test
    void updateToken_저장된_리프레시토큰을_업데이트_할수있다() {
        RefreshTokenEntity updateRequest = RefreshTokenEntity.builder()
                .tokenKey(signedUpUser.getUserId())
                .tokenValue("updateRefreshToken")
                .build();


        sut.updateToken(updateRequest);
        Optional<RefreshTokenEntity> updatedTokenEntity = sut.findTokenByKey(signedUpUser.getUserId());


        assertThat(updatedTokenEntity.isPresent()).isTrue();
        assertThat(updatedTokenEntity.get().getTokenKey()).isEqualTo(updateRequest.getTokenKey());
        assertThat(updatedTokenEntity.get().getTokenValue()).isEqualTo(updateRequest.getTokenValue());
    }

    @Test
    void deleteToken_저장된_리프레시토큰을_삭제할수있다() {
        String targetUserId = signedUpUser.getUserId();


        sut.deleteToken(targetUserId);
        Optional<RefreshTokenEntity> deletedTokenEntity = sut.findTokenByKey(signedUpUser.getUserId());


        assertThat(deletedTokenEntity.isEmpty()).isTrue();
    }
}