package com.canarypets.backend.controllers;

import com.canarypets.backend.models.Product;
import com.canarypets.backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/producto")
public class ProductController {
    @Autowired
    ProductService productService;

    /*@GetMapping("/{productSlug}")
    public String view(@PathVariable String productSlug) {
        return "producto/producto"; // o volver atrás
    }*/
    @GetMapping("/{slug}")
    public String productDetail(@PathVariable String slug, Model model) {

        Product product = productService.getBySlug(slug);

        model.addAttribute("product", product);

        return "producto/producto";
    }
}
