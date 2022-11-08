package com.atoz.user;

import com.atoz.authentication.AuthenticationConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private final UserService userService;

    @PostMapping("/signup")
    public UserResponseDTO signup(@Validated @RequestBody SignupDTO signupDTO) {
        return userService.signup(signupDTO);
    }

    @PostMapping("/signin")
    public UserResponseDTO signin(@Validated @RequestBody SigninDTO signinDTO,
                                  HttpServletRequest request) {
        UserResponseDTO responseData = userService.signin(signinDTO);

        HttpSession session = request.getSession();
        session.setAttribute(AuthenticationConst.SIGNIN_MEMBER, responseData);

        return responseData;
    }

    @PostMapping("/signout")
    public ResponseEntity<Object> signout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Map<String, String> responseBodyMap = new HashMap<>();
        responseBodyMap.put("message", "signout success");

        return ResponseEntity.ok().build();
    }
}
