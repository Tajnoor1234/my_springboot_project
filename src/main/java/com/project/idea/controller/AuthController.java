package com.project.idea.controller;

import com.project.idea.model.User1;
import com.project.idea.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Authentication auth, Model model) {
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/dashboard";
        }
        model.addAttribute("page", "login");
        return "app";
    }

    @GetMapping("/signup")
    public String signup(Authentication auth, Model model) {
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/dashboard";
        }
        model.addAttribute("page", "signup");
        model.addAttribute("user", new User1());
        return "app";
    }

    @PostMapping("/signup")
    public String doSignup(@ModelAttribute User1 user) {
        userService.save(user);
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }
        if (auth.getAuthorities().toString().contains("TEACHER")) {
            return "redirect:/teacher";
        }
        return "redirect:/student";
    }
}
