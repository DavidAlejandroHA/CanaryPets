package com.canarypets.backend.repositories;

import com.canarypets.backend.models.Category;
import com.canarypets.backend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {
    List<Product> findByCategory(Category category);
    List<Product> findByCategoryIn(List<Category> categories); // Para categorías padre
    boolean existsBySlug(String slug);
    Optional<Product> findBySlug(String slug);
}
