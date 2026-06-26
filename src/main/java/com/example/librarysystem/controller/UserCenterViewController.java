package com.example.librarysystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserCenterViewController {

    @GetMapping("/user/center")
    public String userCenterPage(Model model) {
        model.addAttribute("currentPage", "center");
        return "user-center";
    }
}
