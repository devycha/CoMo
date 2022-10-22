package com.dongjji.como.user.controller;

import com.dongjji.como.user.dto.LoginUserDto;
import com.dongjji.como.user.dto.RegisterUserDto;
import com.dongjji.como.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    @GetMapping("/user/login")
    public String getLoginPage(Authentication authentication) {
        return "user/login";
    }

    @PostMapping("/user/login")
    public void login(LoginUserDto loginUserDto) {
    }

    @GetMapping("/user/register")
    public String getRegisterPage() {
        log.info("회원가입 페이지 이동");
        return "user/register";
    }

    @PostMapping("/user/register")
    public String register(RegisterUserDto registerUserDto, HttpServletRequest req) {
        userService.register(registerUserDto);

        return "redirect:/home";
    }
}
