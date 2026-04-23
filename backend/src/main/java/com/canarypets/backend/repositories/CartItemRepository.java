package com.canarypets.backend.repositories;

import com.canarypets.backend.models.CartItem;
import com.canarypets.backend.models.Product;
import com.canarypets.backend.models.ShoppingCart;
import com.canarypets.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(ShoppingCart cart, Product product);
    //List<CartItem> findByUser(User user);
    // Evitar N+1 queries (muy importante en checkout)
    // CartItem -> pertenece a -> ShoppingCart -> pertenece a -> User
    //@Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product WHERE ci.cart.user = :user") // No evita n+1 con el cart
    @Query("""
        SELECT ci 
        FROM CartItem ci 
        JOIN FETCH ci.product 
        JOIN FETCH ci.cart c 
        WHERE c.user = :user
    """)
    List<CartItem> findByUserWithProduct(@Param("user") User user);
}