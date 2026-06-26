package com.example.librarysystem.controller;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.exception.UserAlreadyExistsException;
import com.example.librarysystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {

    private final UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("currentPage", "home");
        return "index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("currentPage", "home");
        return "index";
    }

    @GetMapping("/user/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/user/register")
    public String registerSubmit(@ModelAttribute User user,
                                 RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(
                    user.getUsername(), user.getPassword(), user.getEmail(),
                    user.getFirstName(), user.getLastName()
            );
            redirectAttributes.addFlashAttribute("successMessage", "注册成功！请登录。");
            return "redirect:/user/login";
        } catch (UserAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/user/register";
        }
    }

    @GetMapping("/access-denied")
    @PostMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    @GetMapping("/error")
    public String errorPage() {
        return "error";
    }
}
