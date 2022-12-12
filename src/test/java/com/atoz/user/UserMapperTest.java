package com.atoz.user;

import com.atoz.user.dto.request.ChangePasswordRequestDto;
import com.atoz.user.dto.request.UpdateUserRequestDto;
import com.atoz.user.dto.UserDto;
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
        UserDto userDto = UserDto.builder()
                .userId("newUserId")
                .password("newPassword")
                .nickname("newNickname")
                .email("newEmail@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();


        sut.addUser(userDto);
        sut.addAuthority(userDto);


        Optional<UserDto> addedUser = sut.findById(userDto.getUserId());
        assertThat(addedUser.isPresent()).isTrue();
        assertEquals(userDto.getUserId(), addedUser.get().getUserId());
    }

    @Test
    void addUser_중복된_사용자를_저장하면_예외가_발생한다() {
        UserDto signedUpUser = UserDto.builder()
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
        String userId = "testUserId";
        addUser(userId);


        Optional<UserDto> foundUser = sut.findById(userId);


        assertTrue(foundUser.isPresent());
        assertEquals(userId, foundUser.get().getUserId());
    }

    @Test
    void findById_존재하지_않는_사용자_정보를_조회하면_null이_반환된다() {
        String targetUserId = "wrongUserId";


        Optional<UserDto> foundUser = sut.findById(targetUserId);


        assertTrue(foundUser.isEmpty());
    }

    @Test
    void changePassword_비밀번호가_변경된다() {
        String userId = "testUserId";
        addUser(userId);

        ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
                .userId(userId)
                .password("changedPassword")
                .build();


        sut.changePassword(changePasswordRequestDto);


        Optional<UserDto> updatedUser = sut.findById(userId);
        assertTrue(updatedUser.isPresent());
        assertEquals(changePasswordRequestDto.getPassword(), updatedUser.get().getPassword());
    }

    @Test
    void updateUser_닉네임이_변경된다() {
        String userId = "testUserId";
        addUser(userId);

        UpdateUserRequestDto updateUserRequestDto = UpdateUserRequestDto.builder()
                .userId(userId)
                .nickname("updatedNickname")
                .email("")
                .build();


        sut.updateUser(updateUserRequestDto);


        Optional<UserDto> updatedUser = sut.findById(userId);
        assertTrue(updatedUser.isPresent());
        assertEquals(updateUserRequestDto.getNickname(), updatedUser.get().getNickname());
    }

    @Test
    void updateUser_이메일이_변경된다() {
        String userId = "testUserId";
        addUser(userId);

        UpdateUserRequestDto updateUserRequestDto = UpdateUserRequestDto.builder()
                .userId(userId)
                .nickname("")
                .email("updated@test.com")
                .build();


        sut.updateUser(updateUserRequestDto);


        Optional<UserDto> updatedUser = sut.findById(userId);
        assertTrue(updatedUser.isPresent());
        assertEquals(updateUserRequestDto.getEmail(), updatedUser.get().getEmail());
    }

    @Test
    void deleteUser_사용자_정보가_삭제된다() {
        String userId = "testUserId";
        addUser(userId);


        sut.deleteUser(userId);


        Optional<UserDto> foundUser = sut.findById(userId);
        assertTrue(foundUser.isEmpty());
    }

    private void addUser(String userId) {
        UserDto userDto = UserDto.builder()
                .userId(userId)
                .password("testPassword")
                .nickname("testNickname")
                .email("test@test.com")
                .authorities(Set.of(Authority.ROLE_USER))
                .build();
        sut.addUser(userDto);
        sut.addAuthority(userDto);
    }
}