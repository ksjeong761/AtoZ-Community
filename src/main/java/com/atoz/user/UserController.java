package com.atoz.user;

import com.atoz.ApiResponse;
import com.atoz.authentication.AuthenticationConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public ApiResponse<SigninDTO> signin(@Validated @RequestBody SigninDTO signinDTO,
                                         HttpServletRequest request) {

        SigninDTO userSigninDTO = userService.findSigninInfo(signinDTO);

        HttpSession session = request.getSession();
        session.setAttribute(AuthenticationConst.SIGNIN_MEMBER, userSigninDTO);

        SigninDTO responseData = SigninDTO.builder().userId(userSigninDTO.getUserId()).build();

        return ApiResponse.<SigninDTO>builder()
                .message("signin success")
                .data(responseData).build();
    }

    @PostMapping("/signout")
    public ResponseEntity<Object> signout(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Map<String, String> responseBodyMap = new HashMap<>();
        responseBodyMap.put("message", "signout success");

        return ResponseEntity.ok().build();
    }
}
