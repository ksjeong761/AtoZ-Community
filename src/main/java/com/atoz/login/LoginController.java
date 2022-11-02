package com.atoz.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Validated @RequestBody LoginInfo loginInfo,
                                        HttpServletRequest request) {

        LoginInfo userLoginInfo = loginService.getLoginInfo(loginInfo);

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, userLoginInfo);

        Map<String, Object> responseBodyMap = new HashMap<>();
        responseBodyMap.put("message", "login success");

        return ResponseEntity.ok().body(responseBodyMap);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Map<String, String> responseBodyMap = new HashMap<>();
        responseBodyMap.put("message", "logout success");

        return ResponseEntity.ok().build();
    }
}
