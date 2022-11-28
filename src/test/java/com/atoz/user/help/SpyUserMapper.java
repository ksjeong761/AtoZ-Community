package com.atoz.user.help;

import com.atoz.user.entity.Authority;
import com.atoz.user.entity.UserEntity;
import com.atoz.user.UserMapper;

import java.util.*;

public class SpyUserMapper implements UserMapper {
    private int callCount_findById = 0;

    UserEntity signedUpUser = UserEntity.builder()
            .userId("testUserId")
            .password("testPassword")
            .nickname("testNickname")
            .email("test@test.com")
            .authorities(Set.of(Authority.ROLE_USER))
            .build();

    private final List<UserEntity> users = new ArrayList<>();
    private final List<Authority> authorities = new ArrayList<>();

    public SpyUserMapper() {
        users.add(signedUpUser);
        authorities.addAll(signedUpUser.getAuthorities());
    }

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
