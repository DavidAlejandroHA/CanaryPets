package com.canarypets.backend.repositories;

import com.canarypets.backend.models.Category;
import com.canarypets.backend.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByType(String type); // Buscar por type
    boolean existsBySlug(String type); // Buscar por slug
    Optional<Tag> findByName(String name); // Buscar por name
    Optional<Tag> findByTypeAndName(String type, String name); // Buscar por type + name
    List<Tag> findByTypeAndNameIn(String type, List<String> names); // Para filtros múltiples
    boolean existsByTypeAndName(String type, String name); // Para evitar duplicados al hacer seed

    @Query("""
    SELECT DISTINCT t
    FROM Product p
    JOIN p.tags t
    WHERE t.type = :type
    AND p.category = :category
    """) // Esto servirá para mostrar los tags que pertenecen a una sola categoría
    // Es decir, por ejemplo para mostrar SOLO las marcas que están en productos de una categoría
    List<Tag> findTagsByTypeAndCategory(String type, Category category);

    @Query("""
    SELECT DISTINCT t
    FROM Product p
    JOIN p.tags t
    WHERE p.category = :category
    """)
    List<Tag> findTagsByCategory(Category category);

    @Query("""
    SELECT DISTINCT t
    FROM Product p
    JOIN p.tags t
    WHERE p.category IN :categories
    """)
    List<Tag> findTagsByCategories(List<Category> categories);

    @Query("""
    SELECT t.type, t.slug, COUNT(p)
    FROM Product p
    JOIN p.tags t
    WHERE p.category IN :categories
    GROUP BY t.type, t.slug
    """)
    List<Object[]> countTagsByCategories(List<Category> categories);

    Optional<Tag> findByTypeAndSlug(String type, String slug);
}
