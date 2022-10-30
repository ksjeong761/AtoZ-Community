package com.atoz.login;

import com.atoz.user.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final LoginRepository loginRepository;
    private final UserMapper userMapper;
    private final HttpSession httpSession;

    @Transactional
    @Override
    public LoginInfo getLoginInfo(LoginInfo loginInfo) {
        LoginInfo storedLoginInfo = loginRepository.findByUserId(loginInfo.getUserId());

        if (storedLoginInfo == null) {
            log.info("아이디 또는 패스워드 값이 존재하지 않습니다.");
            return null;
        }

        if (!isValidPassword(loginInfo, storedLoginInfo)) {
            log.info("패스워드 값이 일치하지 않습니다.");
            return null;
        }

        return storedLoginInfo;
    }

    private boolean isValidPassword(LoginInfo loginInfo, LoginInfo storedLoginInfo) {
        return loginInfo.getPassword().equals(storedLoginInfo.getPassword());
    }
}
