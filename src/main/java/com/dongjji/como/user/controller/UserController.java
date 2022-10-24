package com.dongjji.como.user.controller;

import com.dongjji.como.user.auth.PrincipalDetails;
import com.dongjji.como.user.dto.LoginUserDto;
import com.dongjji.como.user.dto.RegisterUserDto;
import com.dongjji.como.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    @GetMapping("/user/login")
    public String getLoginPage(Authentication authentication) {
        if (authentication != null) {
            System.out.println(authentication);
            return "redirect:/home";
        }
        return "user/login";
    }

    @PostMapping("/user/login")
    public void login() {
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

    @GetMapping("/user/email-auth")
    public String emailAuth(@RequestParam("auth-key") String uuid, Model model) {
        boolean result = userService.authorizeEmail(uuid);
        model.addAttribute("result", result);

        return "redirect:/user/login";
    }
}
