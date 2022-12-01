package com.atoz.user.helper;

import com.atoz.user.entity.Authority;
import com.atoz.user.entity.UserEntity;
import com.atoz.user.UserMapper;

import java.util.*;

public class SpyUserMapper implements UserMapper {
    private int callCount_findById = 0;

    private final List<UserEntity> users = new ArrayList<>();
    private final List<Authority> authorities = new ArrayList<>();

    @Override
    public void addUser(UserEntity userEntity) {
        users.add(userEntity);
    }

    @Override
    public void addAuthority(UserEntity userEntity) {
        authorities.addAll(userEntity.getAuthorities());
    }

    @Override
    public Optional<UserEntity> findById(String targetUserId) {
        this.callCount_findById++;

        for (var user : users) {
            if (user.getUserId().equals(targetUserId)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public int getCallCount_findById() {
        return callCount_findById;
    }
}
