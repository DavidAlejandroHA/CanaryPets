package com.canarypets.backend.controllers;

import com.canarypets.backend.models.Category;
import com.canarypets.backend.models.Product;
import com.canarypets.backend.models.User;
import com.canarypets.backend.services.CategoryService;
import com.canarypets.backend.services.UserService;
import com.canarypets.backend.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice // Controller global
public class GlobalModelAttributes {

    //private final CategoryService categoryService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    /*public GlobalModelAttributes(CategoryService categoryService) {
        this.categoryService = categoryService;
    }*/

    // El @ModelAttribute se ejecuta antes de cada método_ del controller
    @ModelAttribute("allCategories") // Añade "allCategories" al modelo de todas las vistas de la app en todos los controllers
    public List<Category> populateCategories() {
        return categoryService.getParentCategories();
    }

    @ModelAttribute
    public void addFavorites(Model model, Authentication auth) {

        Set<Long> favoritesIds = new HashSet<>(); // Importante para evitar null con los favoritos
        // if (auth != null && auth.isAuthenticated()) {
        if (AuthUtils.isUserLogged(auth)) {
            User user = userService.getUserByEmail(auth.getName());
            favoritesIds = user.getFavorites()
                    .stream()
                    .map(Product::getId)
                    .collect(Collectors.toSet());
        }
        model.addAttribute("favoritesIds", favoritesIds);
    }
}