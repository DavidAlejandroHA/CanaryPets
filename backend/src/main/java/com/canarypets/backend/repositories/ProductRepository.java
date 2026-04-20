package com.canarypets.backend.repositories;

import com.canarypets.backend.models.Category;
import com.canarypets.backend.models.Product;
import jakarta.persistence.LockModeType;
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
}
