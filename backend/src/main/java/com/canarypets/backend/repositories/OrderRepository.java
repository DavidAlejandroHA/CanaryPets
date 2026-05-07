package com.canarypets.backend.repositories;

import com.canarypets.backend.enums.OrderStatus;
import com.canarypets.backend.models.Order;
import com.canarypets.backend.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    Page<Order> findByStatus(String status, Pageable pageable);

    @Query("""
    SELECT o FROM Order o
    WHERE LOWER(o.nombre) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(o.apellidos) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(o.email) LIKE LOWER(CONCAT('%', :search, '%'))
""")
    Page<Order> searchOrders(@Param("search") String search, Pageable pageable);

    @Query("""
        SELECT o FROM Order o
        WHERE (:status IS NULL OR o.status = :status)
    
          AND (
                :search IS NULL
                OR LOWER(o.nombre) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(o.apellidos) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(o.email) LIKE LOWER(CONCAT('%', :search, '%'))
          )
    
          AND (
                :dateFrom IS NULL OR o.createdAt >= :dateFrom
          )
    
          AND (
                :dateTo IS NULL OR o.createdAt <= :dateTo
          )
    """)
    Page<Order> searchByFilters(
            @Param("status") OrderStatus status,
            @Param("search") String search,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo,
            Pageable pageable);
}