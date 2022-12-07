package com.atoz.user;

import com.atoz.user.dto.ChangePasswordDTO;
import com.atoz.user.dto.UserResponseDTO;
import com.atoz.user.dto.UserUpdateDTO;
import com.atoz.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDTO signup(UserEntity userEntity) {
        userMapper.addUser(userEntity);
        userMapper.addAuthority(userEntity);

        return userEntity.toResponseDto();
    }

    @Override
    public void update(UserUpdateDTO updateDTO) {

    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO) {

    }

    @Override
    public void delete(String userId) {

    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userMapper.findById(userId);
        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException("해당 유저가 존재하지 않습니다.");
        }

        return userEntity.get().toUserDetails();
    }
}
