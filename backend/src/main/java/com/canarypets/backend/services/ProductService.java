package com.canarypets.backend.services;

import com.canarypets.backend.models.Category;
import com.canarypets.backend.models.Product;
import com.canarypets.backend.repositories.CategoryRepository;
import com.canarypets.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Productos por subcategoría (caso normal)
    public List<Product> getByCategorySlug(String slug) {
       // Category category = categoryRepository.findBySlug(slug)
       //         .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        Optional<Category> category = categoryRepository.findBySlug(slug);
        if (category.isEmpty()) {
            return new ArrayList<>(); // Lista vacía en vez de error
        }
        return productRepository.findByCategory(category.get());
    }

    // Productos por categoría padre (incluye subcategorías)
    public List<Product> getByParentAndChildSlug(String parentSlug, String childSlug) {
        /*Category category = categoryRepository
                .findBySlugAndParent_Slug(childSlug, parentSlug).orElseThrow(() -> new RuntimeException("Subcategoría no válida"));*/
        Optional<Category> category = categoryRepository.findBySlugAndParent_Slug(childSlug, parentSlug);
        if (category.isEmpty()) {
            return new ArrayList<>(); // Lista vacía en vez de error
        }
        return productRepository.findByCategory(category.get());
    }

    // // Obtener todos los productos de una categoría padre
    public List<Product> getAllByParentSlug(String parentSlug) {
        Optional<Category> parentOpt = categoryRepository.findBySlug(parentSlug);

        if (parentOpt.isEmpty()) {
            return new ArrayList<>(); // Lista vacía en vez de error
        }
        Category parent = parentOpt.get();
        List<Category> subcategories = parent.getSubcategories();

        if (subcategories == null || subcategories.isEmpty()) {
            return new ArrayList<>();
        }

        return productRepository.findByCategoryIn(subcategories);
    }
}
