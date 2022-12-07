package com.atoz.user;

import com.atoz.user.dto.*;
import com.atoz.user.entity.Authority;
import com.atoz.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @PostMapping("/signup")
    public UserResponseDTO signup(@Validated @RequestBody SignupDTO signupDTO) {
        UserEntity userEntity = UserEntity.builder()
                .userId(signupDTO.getUserId())
                .password(passwordEncoder.encode(signupDTO.getPassword()))
                .email(signupDTO.getEmail())
                .nickname(signupDTO.getEmail())
                .authorities(Set.of(Authority.ROLE_USER))
                .build();

        return userService.signup(userEntity);
    }

    @PatchMapping
    public void update(@Validated @RequestBody UserUpdateDTO userUpdateDTO) {
        userService.update(userUpdateDTO);
    }

    @PatchMapping("/password")
    public void changePassword(@Validated @RequestBody ChangePasswordDTO changePasswordDTO) {
        ChangePasswordDTO encodedPassword = changePasswordDTO.builder()
                .userId(changePasswordDTO.getUserId())
                .password(passwordEncoder.encode(changePasswordDTO.getPassword()))
                .build();

        userService.changePassword(encodedPassword);
    }

    @DeleteMapping
    public void delete(@Validated @RequestBody UserDeleteDTO deleteDTO) {
        userService.delete(deleteDTO.getUserId());
    }
}