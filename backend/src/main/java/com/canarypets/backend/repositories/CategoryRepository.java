package com.canarypets.backend.repositories;

import com.canarypets.backend.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findBySlug(String slug); // No evita slugs duplicados
    Optional<Category> findBySlugAndParent_Slug(String slug, String parentSlug);
}
