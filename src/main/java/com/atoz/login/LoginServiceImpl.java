package com.atoz.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final LoginMapper loginMapper;

    @Transactional
    @Override
    public LoginInfo getLoginInfo(LoginInfo loginInfo) {
        log.info("LoginServiceImpl.getLoginInfo");

        LoginInfo storedLoginInfo = loginMapper.findById(loginInfo.getUserId());

        if (storedLoginInfo == null) {
            throw new LoginValidationException("해당 유저가 존재하지 않습니다.");
        }

        if (!isValidPassword(loginInfo, storedLoginInfo)) {
            throw new LoginValidationException("패스워드 값이 일치하지 않습니다.");
        }

        return storedLoginInfo;
    }

    private boolean isValidPassword(LoginInfo loginInfo, LoginInfo storedLoginInfo) {
        return loginInfo.getPassword().equals(storedLoginInfo.getPassword());
    }
}
