package com.canarypets.backend.repositories;

import com.canarypets.backend.models.Category;
import com.canarypets.backend.models.Product;
import com.canarypets.backend.models.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {
    List<Product> findByCategory(Category category);
    List<Product> findByCategoryIn(List<Category> categories); // Para categorías padre
    boolean existsBySlug(String slug);
    Optional<Product> findBySlug(String slug);


    // PESSIMISTIC LOCK
    //@Lock(LockModeType.PESSIMISTIC_WRITE)
    //@Query("SELECT p FROM Product p WHERE p.id = :id")
    //Optional<Product> findByIdForUpdate(@Param("id") Long id); // Para evitar race conditions

    @Query("""
        SELECT p FROM User u
        JOIN u.favorites p
        WHERE u = :user
    """)
    Page<Product> findFavoritesByUser(@Param("user") User user, Pageable pageable);

    // Sería necesario para cargar los tags con el Product si el Producto no tuviera
    // el campo tags como Lazy
    //@Query("SELECT p FROM Product p LEFT JOIN FETCH p.tags WHERE p.id = :id")
    //Optional<Product> findByIdWithTags(Long id);

    @Query(value = "SELECT * FROM PRODUCTOS ORDER BY RAND() LIMIT 4", nativeQuery = true)
    List<Product> findRandomProducts();
}
