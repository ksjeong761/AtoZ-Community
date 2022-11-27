package com.atoz.user;

import com.atoz.user.dto.UserResponseDTO;
import com.atoz.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 사용자 정보가 있다면 UserDetails로 변환하여 인증에 사용한다.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserEntity userEntity = userMapper.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다."));

        return userEntity.toUserDetails();
    }
}
