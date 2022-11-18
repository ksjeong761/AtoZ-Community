package com.atoz.authentication;

import com.atoz.user.SigninDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public TokenDTO signin(@Validated @RequestBody SigninDTO signinDTO) {
        return authService.signin(signinDTO);
    }

    @DeleteMapping("/signout")
    public AuthResponseDTO logout(@RequestBody TokenRequestDTO tokenRequestDTO) {
        authService.signout(tokenRequestDTO);
        return new AuthResponseDTO("로그아웃되었습니다.");
    }

    @PostMapping("/refresh")
    public TokenDTO refresh(@RequestBody TokenRequestDTO tokenRequestDTO) {
        return authService.refresh(tokenRequestDTO);
    }
}
