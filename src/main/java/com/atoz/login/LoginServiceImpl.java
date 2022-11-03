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
    public LoginRequestDTO getLoginInfo(LoginRequestDTO loginRequestDTO) {
        LoginRequestDTO storedLoginRequestDTO = loginMapper.findById(loginRequestDTO.getUserId());

        if (storedLoginRequestDTO == null) {
            throw new LoginValidationException("해당 유저가 존재하지 않습니다.");
        }

        if (!isValidPassword(loginRequestDTO, storedLoginRequestDTO)) {
            throw new LoginValidationException("패스워드 값이 일치하지 않습니다.");
        }

        return storedLoginRequestDTO;
    }

    private boolean isValidPassword(LoginRequestDTO loginRequestDTO, LoginRequestDTO storedLoginRequestDTO) {
        return loginRequestDTO.getPassword().equals(storedLoginRequestDTO.getPassword());
    }
}
