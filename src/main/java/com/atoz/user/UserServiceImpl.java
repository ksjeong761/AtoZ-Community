package com.atoz.user;

import com.atoz.error.SigninFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserMapper userMapper;

    public UserResponseDTO signup(SignupDTO signupDTO) {
        userMapper.addUser(signupDTO);

        return new UserResponseDTO(signupDTO);
    }

    @Transactional
    @Override
    public SigninDTO findSigninInfo(SigninDTO signinDTO) {
        SigninDTO storedSigninDTO = userMapper.findById(signinDTO.getUserId());

        if (storedSigninDTO == null) {
            throw new SigninFailedException("해당 유저가 존재하지 않습니다.");
        }

        if (!isValidPassword(signinDTO, storedSigninDTO)) {
            throw new SigninFailedException("패스워드 값이 일치하지 않습니다.");
        }

        return storedSigninDTO;
    }

    private boolean isValidPassword(SigninDTO signinDTO, SigninDTO storedSigninDTO) {
        return signinDTO.getPassword().equals(storedSigninDTO.getPassword());
    }
}
