package com.canarypets.backend.repositories;

import com.canarypets.backend.models.Order;
import com.canarypets.backend.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    @Query("""
        SELECT o FROM Order o
        LEFT JOIN FETCH o.items
        WHERE o.user = :user
        ORDER BY o.createdAt DESC
    """)
    List<Order> findByUserWithItems(@Param("user") User user);

    @Query("""
        SELECT o FROM Order o
        LEFT JOIN FETCH o.items i
        LEFT JOIN FETCH i.product
        WHERE o.id = :id AND o.user = :user
    """)
    Optional<Order> findByIdAndUserWithItems(@Param("id") Long id, @Param("user") User user);

    Page<Order> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}