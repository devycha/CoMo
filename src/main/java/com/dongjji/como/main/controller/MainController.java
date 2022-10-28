package com.dongjji.como.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String getHome() {
        return "home";
    }

    @GetMapping("/home")
    public String getHome2() {
        return "home";
    }
}
