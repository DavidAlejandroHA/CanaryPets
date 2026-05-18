package com.canarypets.backend.controllers;

import com.canarypets.backend.models.Product;
import com.canarypets.backend.services.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String index(@NotNull Model model, HttpSession session) { // Refrescar productos recomendados
        List<Product> recommended;                                  // por sesión

        if (session.getAttribute("recommendedProducts") == null) {
            recommended = productService.getRecommendedProducts();
            session.setAttribute("recommendedProducts", recommended);
        } else {
            recommended = (List<Product>) session.getAttribute("recommendedProducts");
        }

        model.addAttribute("recommendedProducts", recommended);
        return "index";
    }

    @GetMapping("/home")
    public String home(@NotNull Model model) {
        return "redirect:/";
    }

    @GetMapping("/") // Para el healthcheck (no es necesario del todo_ pero por si acaso)
    public String health() {
        return "OK";
    }
}