package com.atoz.user.helper;

import com.atoz.user.dto.ChangePasswordDTO;
import com.atoz.user.dto.UserUpdateDTO;
import com.atoz.user.entity.Authority;
import com.atoz.user.entity.UserEntity;
import com.atoz.user.UserMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

public class SpyUserMapper implements UserMapper {
    public int callCount_findById = 0;

    private final Map<String, UserEntity> users = new HashMap<>();
    private final Map<String, Set<Authority>> authorities = new HashMap<>();

    @Override
    public void addUser(UserEntity userEntity) {
        users.put(userEntity.getUserId(), userEntity);
    }

    @Override
    public void addAuthority(UserEntity userEntity) {
        authorities.put(userEntity.getUserId(), userEntity.getAuthorities());
    }

    @Override
    public Optional<UserEntity> findById(String targetUserId) {
        callCount_findById++;

        var user = users.getOrDefault(targetUserId, null);
        Optional<UserEntity> foundUser = Optional.ofNullable(user);
        if (foundUser.isEmpty()) {
            throw new UsernameNotFoundException("해당 유저가 존재하지 않습니다.");
        }

        return foundUser;
    }

    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        UserEntity before = findById(userUpdateDTO.getUserId()).get();
        UserEntity after = UserEntity.builder()
                .userId(userUpdateDTO.getUserId())
                .password(before.getPassword())
                .nickname(userUpdateDTO.getNickname())
                .authorities(before.getAuthorities())
                .build();

        users.put(userUpdateDTO.getUserId(), after);
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        UserEntity before = findById(changePasswordDTO.getUserId()).get();
        UserEntity after = UserEntity.builder()
                .userId(changePasswordDTO.getUserId())
                .password(before.getPassword())
                .nickname(before.getNickname())
                .authorities(before.getAuthorities())
                .build();

        users.put(changePasswordDTO.getUserId(), after);
    }

    @Override
    public void deleteUser(String targetUserId) {
        users.remove(targetUserId);
    }
}
