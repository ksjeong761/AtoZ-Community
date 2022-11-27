package com.atoz.user.help;

import com.atoz.user.entity.Authority;
import com.atoz.user.entity.UserEntity;
import com.atoz.user.UserMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SpyStubUserMapper implements UserMapper {
    private int callFindByIdCount = 0;

    @Override
    public void addUser(UserEntity userEntity) {
    }

    @Override
    public void addAuthority(UserEntity userEntity) {
    }

    @Override
    public Optional<UserEntity> findById(String userId) {
        this.callFindByIdCount++;

        if (userId.equals("testId")) {
            Set<Authority> authorities = new HashSet<>();
            authorities.add(Authority.ROLE_USER);
            return Optional.ofNullable(
                    new UserEntity("testId",
                            "testPassword",
                            "testNickname",
                            "test@test.com",
                            authorities));
        } else {
            return null;
        }
    }

    public int getCallFindByIdCount() {
        return callFindByIdCount;
    }
}
