package com.atoz.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private final UserMapper userMapper;

    public UserResponseDTO register(UserRequestDTO userRequestDTO) {
        userMapper.addUser(userRequestDTO);

        return new UserResponseDTO(userRequestDTO);
    }
}
