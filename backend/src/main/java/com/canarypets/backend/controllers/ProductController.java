package com.canarypets.backend.controllers;

import com.canarypets.backend.models.Product;
import com.canarypets.backend.models.User;
import com.canarypets.backend.services.ProductService;
import com.canarypets.backend.services.UserService;
import com.canarypets.backend.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    @Autowired
    UserService userService;

    @GetMapping("/{slug}")
    public String productDetail(@PathVariable String slug,
                                Model model,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getBySlug(slug);
            model.addAttribute("product", product);

            boolean isFavorite = false;

            if (AuthUtils.isUserLogged(auth)) {
                User user = userService.getUserByEmail(auth.getName());
                isFavorite = userService.isFavorite(user, product.getId());
            }

            model.addAttribute("isFavorite", isFavorite);

            return "producto/producto";

        } catch (IllegalArgumentException e) {
            //return "error/404"; // vista personalizada
            // Mensaje flash
            redirectAttributes.addFlashAttribute("toastError", e.getMessage());
            return "category/category";
        }
    }
}
