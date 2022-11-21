package com.atoz.user;

import com.atoz.authentication.entity.Authority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO signup(SignupDTO signupDTO) {

        Set<Authority> authorities = new HashSet<>();
        authorities.add(Authority.ROLE_USER);

        UserEntity userEntity = new UserEntity(passwordEncoder, signupDTO, authorities);
        userMapper.addUser(userEntity);
        userMapper.addAuthority(userEntity);

        return new UserResponseDTO(signupDTO);
    }

    private boolean isValidPassword(SigninDTO signinDTO, SigninDTO storedSigninDTO) {
        return signinDTO.getPassword().equals(storedSigninDTO.getPassword());
    }
}
