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
    public LoginDTO getLoginInfo(LoginDTO loginDTO) {
        LoginDTO storedLoginDTO = loginMapper.findById(loginDTO.getUserId());

        if (storedLoginDTO == null) {
            throw new LoginValidationException("해당 유저가 존재하지 않습니다.");
        }

        if (!isValidPassword(loginDTO, storedLoginDTO)) {
            throw new LoginValidationException("패스워드 값이 일치하지 않습니다.");
        }

        return storedLoginDTO;
    }

    private boolean isValidPassword(LoginDTO loginDTO, LoginDTO storedLoginDTO) {
        return loginDTO.getPassword().equals(storedLoginDTO.getPassword());
    }
}
