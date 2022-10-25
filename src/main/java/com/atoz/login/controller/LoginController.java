package com.atoz.login.controller;

import com.atoz.login.annotation.checkLogin;
import com.atoz.login.entity.LoginInfo;
import com.atoz.login.service.LoginService;
import com.atoz.login.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginInfo loginInfo,
                        BindingResult bindingResult,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        log.info("LoginController.login");

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "fail";
        }

        LoginInfo userLoginInfo = loginService.getLoginInfo(loginInfo);

        if (userLoginInfo == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            log.info("errors={}", bindingResult);
            return "fail";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, userLoginInfo);

        return "login success";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "logout success";
    }

    @GetMapping("/")
    public String loginHome(@checkLogin LoginInfo loginInfo, Model model) {

        if (loginInfo == null) {
            return "go home";
        }

        model.addAttribute("loginInfo", loginInfo);

        return "go login home";
    }
}
