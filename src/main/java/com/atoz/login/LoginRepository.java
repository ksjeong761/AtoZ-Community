package com.atoz.login;

import com.atoz.login.LoginInfo;

public interface LoginRepository {
    LoginInfo findByUserId(String userId);
}
