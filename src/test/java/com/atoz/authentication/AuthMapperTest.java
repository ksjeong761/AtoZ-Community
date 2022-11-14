package com.atoz.authentication;

import com.atoz.user.SignupDTO;
import com.atoz.user.UserEntity;
import com.atoz.user.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "/application-test.yaml")
@MybatisTest
class AuthMapperTest {

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private UserMapper userMapper;

    private TokenProvider tokenProvider = new TokenProvider(
            "aGVsbG8tbXktcmVhbC1uYW1lLWlzLXdvbmp1bi10aGlzLWtleS1pcy12ZXJ5LWltcG9ydGFudC1zby1iZS1jYXJlZnVsLXRoYW5rLXlvdQ==",
            1800000,
            604800000);

    PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

    SignupDTO signupDTO;

    @BeforeEach
    public void beforeEach() {
        signupDTO = new SignupDTO(
                "testId", "testPassword",
                "testNickname", "test@test.com"
        );
    }

    @Test
    void findById_사용자정보를_조회할수있다() {
        UserEntity userEntity = new UserEntity(passwordEncoder, signupDTO);
        userMapper.addUser(userEntity);
        userMapper.addAuthority(userEntity);

        JwtSigninDTO jwtSigninDTO = authMapper.findById(userEntity.getUserId()).orElse(null);

        assertThat(jwtSigninDTO).isNotNull();
        assertThat(jwtSigninDTO.getUserId()).isEqualTo(userEntity.getUserId());
    }

    @Test
    void findById_사용자정보가_없는_경우_null을_반환한다() {
        String foundNotUserId = "testId";

        JwtSigninDTO jwtSigninDTO = authMapper.findById(foundNotUserId).orElse(null);

        assertThat(jwtSigninDTO).isNull();
    }

    @Test
    void findByKey_저장된_리프레시토큰을_조회할수있다() {
        UserEntity userEntity = new UserEntity(passwordEncoder, signupDTO);
        userMapper.addUser(userEntity);
        userMapper.addAuthority(userEntity);

        Set<Authority> auths = new HashSet<>();
        auths.add(userEntity.getAuthority());
        String tokenValue = tokenProvider.createRefreshToken(userEntity.getUserId(), auths);
        RefreshToken expectedToken = RefreshToken.builder().tokenKey(userEntity.getUserId()).tokenValue(tokenValue).build();
        authMapper.saveRefreshToken(expectedToken);


        RefreshToken actualToken = authMapper.findByKey(userEntity.getUserId()).orElse(null);


        assertThat(actualToken).isNotNull();
        assertThat(actualToken.getTokenKey()).isEqualTo(expectedToken.getTokenKey());
        assertThat(actualToken.getTokenValue()).isEqualTo(expectedToken.getTokenValue());
    }

    @Test
    void findByKey_리프레시토큰이_없는경우_null을_반환한다() {
        String userId = "null";

        RefreshToken actualToken = authMapper.findByKey(userId).orElse(null);

        assertThat(actualToken).isNull();
    }

    @Test
    void updateRefreshToken_저장된리프레시토큰을_업데이트할_수_있다() throws InterruptedException {
        UserEntity userEntity = new UserEntity(passwordEncoder, signupDTO);
        userMapper.addUser(userEntity);
        userMapper.addAuthority(userEntity);

        Set<Authority> auths = new HashSet<>();
        auths.add(userEntity.getAuthority());
        String orgTokenValue = tokenProvider.createRefreshToken(userEntity.getUserId(), auths);
        RefreshToken orgRefreshToken = RefreshToken.builder().tokenKey(userEntity.getUserId()).tokenValue(orgTokenValue).build();
        authMapper.saveRefreshToken(orgRefreshToken);
        Thread.sleep(1000L);


        String updatedTokenValue = tokenProvider.createRefreshToken(userEntity.getUserId(), auths);
        authMapper.updateRefreshToken(
                RefreshToken.builder()
                        .tokenKey(userEntity.getUserId())
                        .tokenValue(updatedTokenValue)
                        .build());


        RefreshToken updatedRefreshToken = authMapper.findByKey(userEntity.getUserId()).orElse(null);
        assertThat(updatedRefreshToken).isNotNull();
        assertThat(updatedRefreshToken.getTokenValue()).isNotEqualTo(orgRefreshToken.getTokenValue());
    }

    @Test
    void deleteRefreshToken_저장된리프레시토큰을_삭제할수있다() throws InterruptedException {
        UserEntity userEntity = new UserEntity(passwordEncoder, signupDTO);
        userMapper.addUser(userEntity);
        userMapper.addAuthority(userEntity);

        Set<Authority> auths = new HashSet<>();
        auths.add(userEntity.getAuthority());
        String orgTokenValue = tokenProvider.createRefreshToken(userEntity.getUserId(), auths);
        RefreshToken orgRefreshToken = RefreshToken.builder().tokenKey(userEntity.getUserId()).tokenValue(orgTokenValue).build();
        authMapper.saveRefreshToken(orgRefreshToken);
        Thread.sleep(2000L);


        authMapper.deleteRefreshToken(userEntity.getUserId());

        RefreshToken updatedRefreshToken = authMapper.findByKey(userEntity.getUserId()).orElse(null);
        assertThat(updatedRefreshToken).isNull();
    }

}