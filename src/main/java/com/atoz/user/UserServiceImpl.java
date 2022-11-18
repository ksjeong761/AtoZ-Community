package com.atoz.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserMapper userMapper;

    public UserResponseDTO signup(SignupDTO signupDTO) {
        UserEntity userEntity = new UserEntity(signupDTO);
        userMapper.addUser(userEntity);

        return new UserResponseDTO(signupDTO);
    }

    private boolean isValidPassword(SigninDTO signinDTO, SigninDTO storedSigninDTO) {
        return signinDTO.getPassword().equals(storedSigninDTO.getPassword());
    }
}
