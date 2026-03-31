package com.canarypets.backend.services;

import com.canarypets.backend.models.Category;
import com.canarypets.backend.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    // Obtener categoría por slug
    public Category getBySlug(String slug) { // No evita slugs duplicados
        //return categoryRepository.findBySlug(slug)
        //        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        Optional<Category> category = categoryRepository.findBySlug(slug);
        if (category.isEmpty()) {
            return new Category(); // Categoría vacía en vez de error
        }
        return categoryRepository.findBySlug(slug).get();
    }

    // Validar jerarquía - Obtener subcategoría por slug y parentslug
    public Category getBySlugAndParent_Slug(String parentSlug, String childSlug) {
        /*return categoryRepository
                .findBySlugAndParent_Slug(childSlug, parentSlug)
                .orElseThrow(() -> new RuntimeException("Subcategoría inválida"));*/
        Optional<Category> category = categoryRepository
                .findBySlugAndParent_Slug(childSlug, parentSlug);
        // Categoría vacía en vez de error
        //return category.orElseGet(Category::new);

        return category.orElseThrow();
    }

    public List<Category> getCategoryWithSubcategories(Category parent) {

        List<Category> categories = new ArrayList<>();
        categories.add(parent);

        if (parent.getSubcategories() != null) {
            categories.addAll(parent.getSubcategories());
        }

        return categories;
    }
}
