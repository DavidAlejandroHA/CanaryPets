package com.canarypets.backend.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
public class CartController {
    @PostMapping("/add")
    public String addToCart(
            @RequestParam Long productId,
            @RequestParam int quantity,
            Authentication authentication
    ) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        // FUTURO:
        // cartService.addProduct(authentication.getName(), productId, quantity);

        return "redirect:/cart"; // o volver atrás
    }
}
