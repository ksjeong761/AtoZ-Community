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
    public Optional<UserDto> findById(String targetUserId) {
        callCountFindById++;

        var user = users.getOrDefault(targetUserId, null);
        Optional<UserDto> foundUser = Optional.ofNullable(user);
        if (foundUser.isEmpty()) {
            throw new UsernameNotFoundException("해당 유저가 존재하지 않습니다.");
        }

        return foundUser;
    }

    @Override
    public void updateUser(UpdateUserRequestDto updateUserRequestDto, String userId) {
        UserDto before = findById(userId).get();
        UserDto after = UserDto.builder()
                .password(before.getPassword())
                .nickname(updateUserRequestDto.getNickname())
                .authorities(before.getAuthorities())
                .build();

        users.put(userId, after);
    }

    @Override
    public void changePassword(ChangePasswordRequestDto changePasswordRequestDto, String userId) {
        UserDto before = findById(userId).get();
        UserDto after = UserDto.builder()
                .password(before.getPassword())
                .nickname(before.getNickname())
                .authorities(before.getAuthorities())
                .build();

        users.put(userId, after);
    }

    @Override
    public void deleteUser(String targetUserId) {
        users.remove(targetUserId);
    }
}
