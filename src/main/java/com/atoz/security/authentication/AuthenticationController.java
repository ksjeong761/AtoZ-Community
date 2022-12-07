package com.atoz.security.authentication;

import com.atoz.security.authentication.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signin")
    public TokenResponseDTO signin(@Validated @RequestBody SigninDTO signinDTO) {
        return authenticationService.signin(signinDTO);
    }

    @DeleteMapping("/signout")
    public AuthResponseDTO logout(@Validated @RequestBody SignoutDTO signoutDTO) {
        authenticationService.signout(signoutDTO);

        return new AuthResponseDTO("로그아웃 되었습니다.");
    }

    @PostMapping("/refresh")
    public TokenResponseDTO refresh(@RequestBody TokenRequestDTO tokenRequestDTO) {
        return authenticationService.refresh(tokenRequestDTO);
    }
}
