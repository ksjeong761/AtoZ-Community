package com.atoz.user.helper;

import com.atoz.user.dto.request.ChangePasswordRequestDto;
import com.atoz.user.dto.request.UpdateUserRequestDto;
import com.atoz.user.Authority;
import com.atoz.user.dto.UserDto;
import com.atoz.user.UserMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

public class SpyUserMapper implements UserMapper {
    public int callCountFindById = 0;

    private final Map<String, UserDto> users = new HashMap<>();
    private final Map<String, Set<Authority>> authorities = new HashMap<>();

    @Override
    public void addUser(UserDto userDto) {
        users.put(userDto.getUserId(), userDto);
    }

    @Override
    public void addAuthority(UserDto userDto) {
        authorities.put(userDto.getUserId(), userDto.getAuthorities());
    }

    @Override
    public Optional<UserDto> findUserByUserId(String targetUserId) {
        callCountFindById++;

        var user = users.getOrDefault(targetUserId, null);
        Optional<UserDto> foundUser = Optional.ofNullable(user);
        if (foundUser.isEmpty()) {
            throw new UsernameNotFoundException("해당 유저가 존재하지 않습니다.");
        }

        return foundUser;
    }

    @Override
    public void updateUser(UpdateUserRequestDto updateUserRequestDto) {
        UserDto before = findUserByUserId(updateUserRequestDto.getUserId()).get();
        UserDto after = UserDto.builder()
                .userId(updateUserRequestDto.getUserId())
                .password(before.getPassword())
                .nickname(updateUserRequestDto.getNickname())
                .email(updateUserRequestDto.getEmail())
                .authorities(before.getAuthorities())
                .build();

        users.put(updateUserRequestDto.getUserId(), after);
    }

    @Override
    public void changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        UserDto before = findUserByUserId(changePasswordRequestDto.getUserId()).get();
        UserDto after = UserDto.builder()
                .userId(changePasswordRequestDto.getUserId())
                .password(changePasswordRequestDto.getPassword())
                .nickname(before.getNickname())
                .authorities(before.getAuthorities())
                .build();

        users.put(changePasswordRequestDto.getUserId(), after);
    }

    @Override
    public void deleteUser(String targetUserId) {
        users.remove(targetUserId);
    }
}
