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

    private final String TEST_USER_ID = "testUserId";

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


        Optional<UserDto> addedUser = sut.findUserByUserId(userDto.getUserId());
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
        addUser(TEST_USER_ID);


        Optional<UserDto> foundUser = sut.findUserByUserId(TEST_USER_ID);


        assertTrue(foundUser.isPresent());
        assertEquals(TEST_USER_ID, foundUser.get().getUserId());
    }

    @Test
    void findById_존재하지_않는_사용자_정보를_조회하면_null이_반환된다() {
        String targetUserId = "wrongUserId";


        Optional<UserDto> foundUser = sut.findUserByUserId(targetUserId);


        assertTrue(foundUser.isEmpty());
    }

    @Test
    void changePassword_비밀번호가_변경된다() {
        addUser(TEST_USER_ID);
        ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
                .userId(TEST_USER_ID)
                .password("changedPassword")
                .build();


        sut.changePassword(changePasswordRequestDto);


        Optional<UserDto> updatedUser = sut.findUserByUserId(TEST_USER_ID);
        assertTrue(updatedUser.isPresent());
        assertEquals(changePasswordRequestDto.getPassword(), updatedUser.get().getPassword());
    }

    @Test
    void updateUser_닉네임이_변경된다() {
        addUser(TEST_USER_ID);
        UpdateUserRequestDto updateUserRequestDto = UpdateUserRequestDto.builder()
                .userId(TEST_USER_ID)
                .nickname("updatedNickname")
                .email("")
                .build();


        sut.updateUser(updateUserRequestDto);


        Optional<UserDto> updatedUser = sut.findUserByUserId(TEST_USER_ID);
        assertTrue(updatedUser.isPresent());
        assertEquals(updateUserRequestDto.getNickname(), updatedUser.get().getNickname());
    }

    @Test
    void updateUser_이메일이_변경된다() {
        addUser(TEST_USER_ID);
        UpdateUserRequestDto updateUserRequestDto = UpdateUserRequestDto.builder()
                .userId(TEST_USER_ID)
                .nickname("")
                .email("updated@test.com")
                .build();


        sut.updateUser(updateUserRequestDto);


        Optional<UserDto> updatedUser = sut.findUserByUserId(TEST_USER_ID);
        assertTrue(updatedUser.isPresent());
        assertEquals(updateUserRequestDto.getEmail(), updatedUser.get().getEmail());
    }

    @Test
    void deleteUser_사용자_정보가_삭제된다() {
        addUser(TEST_USER_ID);


        sut.deleteUser(TEST_USER_ID);


        Optional<UserDto> foundUser = sut.findUserByUserId(TEST_USER_ID);
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