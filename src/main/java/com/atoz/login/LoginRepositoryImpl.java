package com.atoz.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class LoginRepositoryImpl implements LoginRepository {

    private final LoginMapper loginMapper;

    @Override
    public LoginInfo findByUserId(String userId) {
        return loginMapper.findById(userId);
    }
}
