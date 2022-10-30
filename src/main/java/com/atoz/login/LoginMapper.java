package com.atoz.login;

import com.atoz.login.LoginInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {
    LoginInfo findById(String userId);
}
