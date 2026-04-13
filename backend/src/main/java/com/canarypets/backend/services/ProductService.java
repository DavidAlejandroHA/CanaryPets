package com.canarypets.backend.services;

import com.canarypets.backend.models.Category;
import com.canarypets.backend.models.Product;
import com.canarypets.backend.models.Tag;
import com.canarypets.backend.repositories.CategoryRepository;
import com.canarypets.backend.repositories.ProductRepository;
import com.canarypets.backend.repositories.TagRepository;
import com.canarypets.backend.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {

    @Autowired
    private TagRepository tagRepository;

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

    public Map<String, List<Tag>> getAvailableFilters(List<Category> categories) {

        List<Tag> tags = tagRepository.findTagsByCategories(categories);

        Map<String, List<Tag>> filters = new HashMap<>();

        for (Tag tag : tags) {
            filters
                    .computeIfAbsent(tag.getType(), k -> new ArrayList<>())
                    .add(tag);
        }

        return filters;
    }

    public Map<String, Map<String, Long>> getFilterCounts(List<Category> categories) {

        List<Object[]> results = tagRepository.countTagsByCategories(categories);

        Map<String, Map<String, Long>> filters = new HashMap<>();

        for (Object[] row : results) {
            String type = (String) row[0];
            String slug = (String) row[1]; // Antes era name, ahora es slug
            Long count = (Long) row[2];

            filters
                    .computeIfAbsent(type, k -> new HashMap<>())
                    .put(slug, count);
        }

        return filters;
    }

    public String generateUniqueSlug(String baseSlug) {
        /*String slug = baseSlug;
        int i = 1;
        while (productRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + i;
            i++;
        }
        return slug;*/
        return SlugUtils.generateUniqueSlug(baseSlug, s -> productRepository.existsBySlug(s));
        // Evitar duplicados (por si acaso)
    }

    // Recoger categorías más profundas (nietos) // descartado
//    private void collectCategories(Category category, List<Category> list) {
//        list.add(category);
//        if (category.getSubcategories() != null) {
//            for (Category sub : category.getSubcategories()) {
//                collectCategories(sub, list);
//            }
//        }
//    }

    public Product getBySlug(String slug) {
        return productRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }
}
