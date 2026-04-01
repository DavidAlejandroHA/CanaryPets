package com.canarypets.backend.controllers;

import com.canarypets.backend.models.Category;
import com.canarypets.backend.services.CategoryService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice // Controller global
public class GlobalModelAttributes {

    private final CategoryService categoryService;

    public GlobalModelAttributes(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // El @ModelAttribute se ejecuta antes de cada método_ del controller
    @ModelAttribute("allCategories") // Añade "allCategories" al modelo de todas las vistas de la app en todos los controllers
    public List<Category> populateCategories() {
        return categoryService.getParentCategories();
    }
}