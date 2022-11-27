package com.atoz.security.authentication;

import com.atoz.security.authentication.dto.AuthResponseDTO;
import com.atoz.security.authentication.dto.TokenResponseDTO;
import com.atoz.security.authentication.dto.TokenRequestDTO;
import com.atoz.user.dto.SigninDTO;
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
