package com.atoz.login.repository;

import com.atoz.login.entity.LoginInfo;

public interface LoginRepository {
    LoginInfo findByUserId(String userId);
}
