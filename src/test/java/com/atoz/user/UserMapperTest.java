package com.atoz.user;

import com.atoz.user.dto.ChangePasswordDTO;
import com.atoz.user.dto.UpdateUserDTO;
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
    void addUser_사용자_정보가_저장된다() {
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
    void addUser_중복된_사용자를_저장하면_예외가_발생한다() {
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
    void findById_사용자_정보가_조회된다() {
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
    void findById_존재하지_않는_사용자_정보를_조회하면_null이_반환된다() {
        String targetUserId = "wrongUserId";


        Optional<UserEntity> foundUser = sut.findById(targetUserId);


        assertTrue(foundUser.isEmpty());
    }

    @Test
    void changePassword_비밀번호가_변경된다() {
        UserEntity signedUpUser = UserEntity.builder()
                .userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        sut.addUser(signedUpUser);
        sut.addAuthority(signedUpUser);

        ChangePasswordDTO changePasswordDTO = ChangePasswordDTO.builder()
                .userId(signedUpUser.getUserId())
                .password("changedPassword")
                .build();


        sut.changePassword(changePasswordDTO);


        Optional<UserEntity> updatedUser = sut.findById(signedUpUser.getUserId());
        assertTrue(updatedUser.isPresent());
        assertEquals(updatedUser.get().getPassword(), changePasswordDTO.getPassword());
    }

    @Test
    void updateUser_닉네임이_변경된다() {
        UserEntity signedUpUser = UserEntity.builder()
                .userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        sut.addUser(signedUpUser);
        sut.addAuthority(signedUpUser);

        UpdateUserDTO updateDTO = UpdateUserDTO.builder()
                .userId(signedUpUser.getUserId())
                .nickname("updatedNickname")
                .email(signedUpUser.getEmail())
                .build();


        sut.updateUser(updateDTO);


        Optional<UserEntity> updatedUser = sut.findById(signedUpUser.getUserId());
        assertTrue(updatedUser.isPresent());
        assertEquals(updatedUser.get().getNickname(), updateDTO.getNickname());
    }

    @Test
    void updateUser_이메일이_변경된다() {
        UserEntity signedUpUser = UserEntity.builder()
                .userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        sut.addUser(signedUpUser);
        sut.addAuthority(signedUpUser);

        UpdateUserDTO updateDTO = UpdateUserDTO.builder()
                .userId(signedUpUser.getUserId())
                .nickname(signedUpUser.getNickname())
                .email("updated@test.com")
                .build();


        sut.updateUser(updateDTO);


        Optional<UserEntity> updatedUser = sut.findById(signedUpUser.getUserId());
        assertTrue(updatedUser.isPresent());
        assertEquals(updatedUser.get().getEmail(), updateDTO.getEmail());
    }

    @Test
    void deleteUser_사용자_정보가_삭제된다() {
        UserEntity signedUpUser = UserEntity.builder()
                .userId("testUserId")
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        sut.addUser(signedUpUser);
        sut.addAuthority(signedUpUser);


        sut.deleteUser(signedUpUser.getUserId());


        Optional<UserEntity> foundUser = sut.findById(signedUpUser.getUserId());
        assertTrue(foundUser.isEmpty());
    }
}