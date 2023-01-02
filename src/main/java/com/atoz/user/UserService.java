package com.atoz.user;

import com.atoz.user.dto.request.ChangePasswordRequestDto;
import com.atoz.user.dto.request.DeleteUserRequestDto;
import com.atoz.user.dto.response.UserResponseDto;
import com.atoz.user.dto.request.UpdateUserRequestDto;
import com.atoz.user.dto.UserDto;

public interface UserService {

    UserResponseDto signup(UserDto userDto);

    void update(UpdateUserRequestDto updateUserRequestDto);

    void changePassword(ChangePasswordRequestDto changePasswordRequestDto);

    void delete(DeleteUserRequestDto deleteUserRequestDto);
}
