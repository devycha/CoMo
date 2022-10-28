package com.dongjji.como.user.controller;

import com.dongjji.como.user.dto.ChangeUserInfoDto;
import com.dongjji.como.user.dto.MypageUserInfoDto;
import com.dongjji.como.user.dto.RegisterUserDto;
import com.dongjji.como.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    @GetMapping("/user/login")
    public String getLoginPage(Authentication authentication) {
        if (authentication != null) {
            System.out.println(authentication);
            return "redirect:/";
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

        return "redirect:/";
    }

    @GetMapping("/user/email-auth")
    public String emailAuth(@RequestParam("auth-key") String uuid, Model model) {
        log.info("유저 이메일 계정 인증: " + uuid);
        userService.authorizeEmail(uuid);

        return "redirect:/user/login";
    }

    @GetMapping("/user/mypage")
    public String getMypage(Authentication authentication, Model model) {

        if (authentication == null) {
            return "redirect:/user/login";
        }

        String currentUserEmail = getCurrentUserEmail(authentication);
        MypageUserInfoDto userDto = userService.getUserByEmail(currentUserEmail);
        model.addAttribute("user", userDto);

        return "user/mypage.html";
    }

    @PutMapping("/user/change-info")
    public String changeUserInfo(@RequestParam String id,
                                 ChangeUserInfoDto changeUserInfoDto,
                                 Authentication authentication,
                                 Model model) {
        if (authentication == null) {
            return "redirect:/user/login";
        }

        String currentUserEmail = getCurrentUserEmail(authentication);
        MypageUserInfoDto userDto =
                userService.changeUserInfo(id, currentUserEmail, changeUserInfoDto);
        model.addAttribute("user", userDto);

        return "user/mypage.html";
    }

    @DeleteMapping("/user/change-info")
    public String deleteUser(@RequestParam String id,
            Authentication authentication) {
        if (authentication == null) {
            return "redirect:/user/login";
        }

        String currentUserEmail = getCurrentUserEmail(authentication);
        userService.deleteUser(id, currentUserEmail);

        return "redirect:/user/logout";
    }

    private String getCurrentUserEmail(Authentication authentication) {
        if (authentication == null) {
            return null;
        }

        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }
}
