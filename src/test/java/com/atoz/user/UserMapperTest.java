package com.atoz.user;

import com.atoz.user.entity.Authority;
import com.atoz.user.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestPropertySource(locations = "/application-test.yaml")
@MybatisTest
class UserMapperTest {

    @Autowired
    private UserMapper sut;

    @Test
    void addUser_회원가입에_성공해야한다() {
        UserEntity newUser = UserEntity.builder()
                .userId("newUserId")
                .password("newPassword")
                .nickname("newNickname")
                .email("newEmail@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();


        sut.addUser(newUser);
        sut.addAuthority(newUser);


        Optional<UserEntity> addedUser = sut.findById(newUser.getUserId());
        assertThat(addedUser.isPresent()).isTrue();
        assertThat(addedUser.get().getUserId()).isEqualTo(newUser.getUserId());
    }

    @Test
    void addUser_이미_가입되어있다면_회원가입에_실패해야한다() {
        UserEntity signedUpUser = UserEntity.builder()
                .userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        sut.addUser(signedUpUser);
        sut.addAuthority(signedUpUser);


        Throwable thrown = catchThrowable(() -> {
            sut.addUser(signedUpUser);
        });


        assertInstanceOf(DataAccessException.class, thrown);
    }

    @Test
    void findById_사용자정보를_조회할수있다() {
        UserEntity signedUpUser = UserEntity.builder()
                .userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        sut.addUser(signedUpUser);
        sut.addAuthority(signedUpUser);


        Optional<UserEntity> foundUser = sut.findById(signedUpUser.getUserId());


        assertTrue(foundUser.isPresent());
        assertEquals(foundUser.get().getUserId(), signedUpUser.getUserId());
    }

    @Test
    void findById_가입되지않은_사용자정보를_조회할수없다() {
        String targetUserId = "wrongUserId";


        Optional<UserEntity> foundUser = sut.findById(targetUserId);


        assertTrue(foundUser.isEmpty());
    }
}