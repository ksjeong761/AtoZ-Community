package com.atoz.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepository {

    public void addUser(User user) {
        log.info("UserRepository addUser");
    }
}
