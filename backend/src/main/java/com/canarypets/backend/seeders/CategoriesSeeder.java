package com.canarypets.backend.seeders;

import com.canarypets.backend.models.Category;
import com.canarypets.backend.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
public class CategoriesSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            seedCategories();
        }
    }

    private void seedCategories() {
        seedPerros();
        seedGatos();
        seedRoedores();
        seedPeces();
        seedPajaros();
        seedReptiles();
    }

    private void seedPerros() {
        Category perros = createCategory("Perros", "perros", null); // Categoría Perros

        createCategory("Comida", null, perros); // Subcategoría
        createCategory("Accesorios", null, perros);
        createCategory("Juguetes", null, perros);
        createCategory("Salud", null, perros);
        createCategory("Higiene", null, perros);
    }

    private void seedGatos() {
        Category gatos = createCategory("Gatos", "gatos", null); // Categoría Gatos

        createCategory("Comida", null, gatos); // Subcategoría
        createCategory("Accesorios", null, gatos);
        createCategory("Juguetes", null, gatos);
        createCategory("Salud", null, gatos);
        createCategory("Higiene", null, gatos);
    }

    private void seedRoedores() {
        Category roedores = createCategory("Roedores", "roedores", null); // Categoría Roedores

        createCategory("Comida", null, roedores); // Subcategoría
        createCategory("Accesorios", null, roedores);
        createCategory("Higiene", null, roedores);
    }

    private void seedPeces() {
        Category peces = createCategory("Peces", "peces", null); // Categoría Peces

        createCategory("Comida", null, peces); // Subcategoría
        createCategory("Accesorios", null, peces);
        createCategory("Salud", null, peces);
    }

    private void seedPajaros() {
        Category pajaros = createCategory("Pajaros", "pajaros", null); // Categoría Pájaros

        createCategory("Comida", null, pajaros); // Subcategoría
        createCategory("Accesorios", null, pajaros);
        createCategory("Higiene", null, pajaros);
    }

    private void seedReptiles() {
        Category reptiles = createCategory("Reptiles", "reptiles", null); // Categoría Reptiles

        createCategory("Comida", null, reptiles); // Subcategoría
        createCategory("Accesorios", null, reptiles);
        createCategory("Salud", null, reptiles);
    }

    private Category createCategory(String name, String slug, Category parent) {
        Category c = new Category();
        c.setName(name);
        if (slug == null || slug.isEmpty()) c.generateSlug();
        else c.setSlug(slug);
        c.setParent(parent);
        return categoryRepository.save(c);
    }
}