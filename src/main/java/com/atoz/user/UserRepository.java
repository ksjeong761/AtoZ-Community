package com.atoz.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepository {

    @Autowired
    private final UserMapper userMapper;

    public void addUser(User user) {
        log.info("UserRepository addUser : " + user);

        userMapper.addUser(user);
    }
}
