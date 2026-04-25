package com.canarypets.backend.controllers;

import com.canarypets.backend.models.User;
import com.canarypets.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
@PreAuthorize("isAuthenticated()")
public class ProfileController {
    @Autowired
    UserService userService;

    @GetMapping
    public String viewCart(Model model, Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);
        return "profile/profile";
    }
}
