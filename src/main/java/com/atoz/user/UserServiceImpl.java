package com.atoz.user;

import com.atoz.security.token.RefreshTokenMapper;
import com.atoz.user.dto.request.ChangePasswordRequestDto;
import com.atoz.user.dto.response.UserResponseDto;
import com.atoz.user.dto.request.UpdateUserRequestDto;
import com.atoz.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.atoz.security.SecurityUtils.loadUserIdFromContext;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserMapper userMapper;
    private final RefreshTokenMapper refreshTokenMapper;

    @Override
    @Transactional
    public UserResponseDto signup(UserDto userDto) {
        userMapper.addUser(userDto);
        userMapper.addAuthority(userDto);

        return userDto.toUserResponseDto();
    }

    @Override
    @Transactional
    public void update(UpdateUserRequestDto updateUserRequestDto) {
        userMapper.updateUser(updateUserRequestDto, loadUserIdFromContext());
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        userMapper.changePassword(changePasswordRequestDto, loadUserIdFromContext());

        refreshTokenMapper.deleteToken(loadUserIdFromContext());
    }

    @Override
    @Transactional
    public void delete() {
        userMapper.deleteUser(loadUserIdFromContext());

        refreshTokenMapper.deleteToken(loadUserIdFromContext());
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<UserDto> userDto = userMapper.findUserByUserId(userId);
        if (userDto.isEmpty()) {
            throw new UsernameNotFoundException("해당 유저가 존재하지 않습니다.");
        }

        return userDto.get().toUserDetails();
    }
}
