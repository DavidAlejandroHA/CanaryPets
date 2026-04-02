package com.canarypets.backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/producto")
public class ProductController {
    @GetMapping("/{productSlug}")
    public String view(@PathVariable String productSlug) {
        return "producto/producto"; // o volver atrás
    }
}
