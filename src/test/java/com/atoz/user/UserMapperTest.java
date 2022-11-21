package com.atoz.user;

import com.atoz.authentication.entity.Authority;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestPropertySource(locations = "/application-test.yaml")
@MybatisTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;
    
    Set<Authority> authorities = new HashSet<>();

    @BeforeEach
    public void beforeEach() {
        authorities.add(Authority.ROLE_USER);
    }

    @Test
    void addUser_회원가입에_성공해야한다() {
        UserEntity user = UserEntity.builder().userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .authorities(authorities).build();

        userMapper.addUser(user);
        userMapper.addAuthority(user);
        UserEntity addedUser = userMapper.findById(user.getUserId()).orElse(null);

        assertThat(addedUser.getUserId()).isEqualTo("testUserId");
    }

    @Test
    void addUser_이미_가입되어있다면_회원가입에_실패해야한다() {
        UserEntity user = UserEntity.builder().userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .authorities(authorities).build();

        Assertions.assertThatThrownBy(() -> {
            userMapper.addUser(user);
            userMapper.addUser(user);
        }).isInstanceOf(DataAccessException.class);
    }

    @Test
    void findById_사용자정보를_조회할수있다() {
        UserEntity signedUpUser = UserEntity.builder().userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .authorities(authorities).build();
        userMapper.addUser(signedUpUser);
        userMapper.addAuthority(signedUpUser);

        UserEntity foundUser = userMapper.findById(signedUpUser.getUserId()).orElse(null);
        log.info("foundUser id={}, password={}", foundUser.getUserId(), foundUser.getPassword());

        assertThat(signedUpUser.getUserId()).isEqualTo(foundUser.getUserId());
    }

    @Test
    void findById_가입되지않은_사용자정보를_조회할수없다() {
        UserEntity signedUpUser = UserEntity.builder().userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .authorities(authorities).build();
        userMapper.addUser(signedUpUser);
        userMapper.addAuthority(signedUpUser);

        UserEntity findUser = userMapper.findById("wrong userId").orElse(null);

        assertThat(findUser).isNull();
    }
}