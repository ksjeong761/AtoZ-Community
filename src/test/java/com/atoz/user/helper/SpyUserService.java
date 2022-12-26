package com.atoz.user.helper;

import com.atoz.user.UserService;
import com.atoz.user.dto.request.ChangePasswordRequestDto;
import com.atoz.user.dto.response.UserResponseDto;
import com.atoz.user.dto.request.UpdateUserRequestDto;
import com.atoz.user.dto.UserDto;

public class SpyUserService implements UserService {

    public String encodedPassword;

    @Override
    public UserResponseDto signup(UserDto userDto) {
        encodedPassword = userDto.getPassword();

        return null;
    }

    @Override
    public void update(UpdateUserRequestDto updateUserRequestDto) {

    }

    @Override
    public void changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        encodedPassword = changePasswordRequestDto.getPassword();
    }

    @Override
    public void delete() {

    }
}
