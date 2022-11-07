package com.atoz.user;

import com.atoz.cryptography.HashManager;
import com.atoz.error.SigninFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserMapper userMapper;

    public UserResponseDTO signup(SignupDTO signupDTO) {
        userMapper.addUser(new UserEntity(signupDTO));

        return new UserResponseDTO(signupDTO);
    }

    @Transactional
    @Override
    public UserResponseDTO signin(SigninDTO signinDTO) {
        UserEntity user = userMapper.findById(signinDTO.getUserId());

        if (user == null) {
            throw new SigninFailedException("해당 유저가 존재하지 않습니다.");
        }

        if (!isValidPassword(signinDTO, user)) {
            throw new SigninFailedException("패스워드 값이 일치하지 않습니다.");
        }

        return new UserResponseDTO(user);
    }


    private boolean isValidPassword(SigninDTO signinDTO, UserEntity user) {
        HashManager hashManager = new HashManager();
        byte[] hashedPassword = hashManager.hashString(signinDTO.getPassword(), user.getPasswordSalt());

        return Arrays.equals(hashedPassword, user.getPassword());
    }
}
