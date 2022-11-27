package com.atoz.authentication.controller;

import com.atoz.authentication.dto.response.AuthResponseDTO;
import com.atoz.authentication.service.AuthenticationServiceImpl;
import com.atoz.authentication.dto.response.TokenResponseDTO;
import com.atoz.authentication.dto.request.TokenRequestDTO;
import com.atoz.user.SigninDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authServiceImpl;

    @PostMapping("/signin")
    public TokenResponseDTO signin(@Validated @RequestBody SigninDTO signinDTO) {
        return authServiceImpl.signin(signinDTO);
    }

    @DeleteMapping("/signout")
    public AuthResponseDTO logout(@RequestBody TokenRequestDTO tokenRequestDTO) {
        authServiceImpl.signout(tokenRequestDTO);
        return new AuthResponseDTO("로그아웃 되었습니다.");
    }

    @PostMapping("/refresh")
    public TokenResponseDTO refresh(@RequestBody TokenRequestDTO tokenRequestDTO) {
        return authServiceImpl.refresh(tokenRequestDTO);
    }
}
