package com.atoz.security.authentication;

import com.atoz.security.authentication.dto.request.SigninRequestDto;
import com.atoz.security.authentication.dto.request.TokenRequestDto;
import com.atoz.security.authentication.dto.response.AuthResponseDto;
import com.atoz.security.authentication.dto.response.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signin")
    public TokenResponseDto signin(@Validated @RequestBody SigninRequestDto signinRequestDto) {
        return authenticationService.signin(signinRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/signout")
    public AuthResponseDto signout() {
        authenticationService.signout();

        return new AuthResponseDto("로그아웃 되었습니다.");
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/refresh")
    public TokenResponseDto refresh(@RequestBody TokenRequestDto tokenRequestDto) {
        return authenticationService.refresh(tokenRequestDto);
    }
}
