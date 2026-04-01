package com.canarypets.backend.repositories;

import com.canarypets.backend.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findBySlug(String slug); // No evita slugs duplicados
    Optional<Category> findBySlugAndParent_Slug(String slug, String parentSlug);
    //@Query("SELECT c FROM Category c WHERE c.parent IS NULL")
    //List<Category> findParentCategories(); // Devolver solo categorías padre (sin parent)
    List<Category> findByParentIsNull(); // Lo mismo pero sin @Query
}
