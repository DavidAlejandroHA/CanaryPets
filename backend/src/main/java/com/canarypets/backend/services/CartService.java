package com.canarypets.backend.services;

import com.canarypets.backend.models.CartItem;
import com.canarypets.backend.models.Product;
import com.canarypets.backend.models.ShoppingCart;
import com.canarypets.backend.models.User;
import com.canarypets.backend.repositories.CartItemRepository;
import com.canarypets.backend.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductService productService;

    // Obtener carrito del usuario
    public ShoppingCart getCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    ShoppingCart cart = new ShoppingCart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                }); // Crear nuevo carrito si es que el usuario no lo tiene
    }

    // Añadir producto
    public void addProduct(User user, Long productId, int quantity) {
        ShoppingCart cart = getCart(user);
        Product product = productService.getById(productId);

        Optional<CartItem> existing = cartItemRepository.findByCartAndProduct(cart, product);

        CartItem item;
        if (existing.isPresent()) {
            item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);
        }
        cartItemRepository.save(item);
    }

    // Actualizar cantidad
    public void updateQuantity(User user, Long productId, int quantity) {
        ShoppingCart cart = getCart(user);
        Product product = productService.getById(productId);

        CartItem item = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow();

        item.setQuantity(quantity);
        cartItemRepository.save(item);
    }

    // Eliminar item
    public void removeProduct(User user, Long productId) {
        ShoppingCart cart = getCart(user);
        Product product = productService.getById(productId);

        CartItem item = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow();

        cartItemRepository.delete(item);
    }
}