package com.canarypets.backend.controllers;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String index(@NotNull Model model) {
        return "index";
    }

    @GetMapping("/home")
    public String home(@NotNull Model model) {
        return "redirect:/";
    }
}