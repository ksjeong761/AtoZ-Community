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

    @PostMapping("/register")
    public UserResponseDTO register(@Validated @RequestBody RegisterDTO registerDTO) {
        return userService.register(registerDTO);
    }

    @PostMapping("/login")
    public ApiResponse<LoginDTO> login(@Validated @RequestBody LoginDTO loginDTO,
                                       HttpServletRequest request) {

        LoginDTO userLoginDTO = userService.getLoginInfo(loginDTO);

        HttpSession session = request.getSession();
        session.setAttribute(AuthenticationConst.LOGIN_MEMBER, userLoginDTO);

        LoginDTO responseData = LoginDTO.builder().userId(userLoginDTO.getUserId()).build();

        return ApiResponse.<LoginDTO>builder()
                .message("login success")
                .data(responseData).build();
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
