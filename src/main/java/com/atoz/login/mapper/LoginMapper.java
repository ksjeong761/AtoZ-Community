package com.atoz.login.mapper;

import com.atoz.login.entity.LoginInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {
    LoginInfo findById(String userId);
}
