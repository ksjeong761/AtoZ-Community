package com.atoz.login.repository;

import com.atoz.login.mapper.LoginMapper;
import com.atoz.login.entity.LoginInfo;
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
