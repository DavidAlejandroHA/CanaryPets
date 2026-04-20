package com.canarypets.backend.controllers;

import com.canarypets.backend.models.Product;
import com.canarypets.backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/producto")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/{slug}")
    public String productDetail(@PathVariable String slug,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getBySlug(slug);
            model.addAttribute("product", product);
            return "producto/producto";

        } catch (IllegalArgumentException e) {
            //return "error/404"; // vista personalizada
            // Mensaje flash
            redirectAttributes.addFlashAttribute("toastError", e.getMessage());
            return "category/category";
        }
    }
}
