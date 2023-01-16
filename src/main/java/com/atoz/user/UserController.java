package com.atoz.user;

import com.atoz.user.dto.request.ChangePasswordRequestDto;
import com.atoz.user.dto.request.DeleteUserRequestDto;
import com.atoz.user.dto.request.SignupRequestDto;
import com.atoz.user.dto.request.UpdateUserRequestDto;
import com.atoz.user.dto.response.UserResponseDto;
import com.atoz.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @PostMapping("/signup")
    public UserResponseDto signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        UserDto userDto = UserDto.builder()
                .userId(signupRequestDto.getUserId())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .email(signupRequestDto.getEmail())
                .nickname(signupRequestDto.getNickname())
                .age(signupRequestDto.getAge())
                .authorities(Set.of(Authority.ROLE_USER))
                .build();

        return userService.signup(userDto);
    }

    @PreAuthorize("hasRole('USER') and principal.username == #updateUserRequestDto.getUserId()")
    @PatchMapping
    public void update(@Valid @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        userService.update(updateUserRequestDto);
    }

    @PreAuthorize("hasRole('USER') and principal.username == #changePasswordRequestDto.getUserId()")
    @PatchMapping("/password")
    public void changePassword(@Valid @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        ChangePasswordRequestDto encodedPasswordDto = changePasswordRequestDto.builder()
                .password(passwordEncoder.encode(changePasswordRequestDto.getPassword()))
                .build();

        userService.changePassword(encodedPasswordDto);
    }

    @PreAuthorize("hasRole('USER') and principal.username == #deleteUserRequestDto.getUserId()")
    @DeleteMapping
    public void delete(@Valid @RequestBody DeleteUserRequestDto deleteUserRequestDto) {
        userService.delete(deleteUserRequestDto);
    }
}