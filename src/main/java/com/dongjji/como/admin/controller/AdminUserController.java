package com.dongjji.como.admin.controller;

import com.dongjji.como.admin.dto.AdminChangeUserDto;
import com.dongjji.como.admin.dto.AdminUserDto;
import com.dongjji.como.admin.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService adminUserService;

    @GetMapping("/admin/users")
    public String getUserList(Model model) {
        model.addAttribute("users", adminUserService.getAllUsers());
        return "admin/user-list";
    }

    @GetMapping("/admin/user")
    public String getUserById(@RequestParam("id") String userId, Model model) {
        AdminUserDto userDto = adminUserService.getUserById(userId);
        model.addAttribute("user", userDto);
        return "admin/user-detail";
    }

    @PutMapping("/admin/user")
    public String changeUserById(
            @RequestParam("id") String userId,
            AdminChangeUserDto adminChangeUserDto,
            Model model
    ) {
        AdminUserDto userDto = adminUserService.changeUserInfo(userId, adminChangeUserDto);
        model.addAttribute("user", userDto);
        return "admin/user-detail";
    }
}
