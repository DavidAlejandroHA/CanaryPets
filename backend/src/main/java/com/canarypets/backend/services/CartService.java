package com.canarypets.backend.services;

import com.canarypets.backend.models.CartItem;
import com.canarypets.backend.models.Product;
import com.canarypets.backend.models.ShoppingCart;
import com.canarypets.backend.models.User;
import com.canarypets.backend.repositories.CartItemRepository;
import com.canarypets.backend.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUserWithProduct(user);
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

    // Limpiar carrito
    public void clearCart(User user) {
        ShoppingCart cart = getCart(user);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    // Validar carrito
//    public void validateCart(User user) {
//
//        List<CartItem> items = cartItemRepository.findByUserWithProduct(user);
//
//        if (items.isEmpty()) {
//            throw new IllegalStateException("El carrito está vacío");
//        }
//
//        for (CartItem item : items) {
//
//            Product product = item.getProduct();
//
//            // Producto eliminado
//            if (product == null) {
//                throw new IllegalStateException("Producto no disponible");
//            }
//
//            // Sin stock
//            if (product.getStock() <= 0) {
//                throw new IllegalStateException(
//                        "El producto '" + product.getName() + "' está sin stock"
//                );
//            }
//
//            // Cantidad mayor que stock
//            if (item.getQuantity() > product.getStock()) {
//                throw new IllegalStateException(
//                        "No hay suficiente stock para '" + product.getName() +
//                                "'. Disponible: " + product.getStock()
//                );
//            }
//
//            // Cantidad inválida
//            if (item.getQuantity() <= 0) {
//                throw new IllegalStateException(
//                        "Cantidad inválida para '" + product.getName() + "'"
//                );
//            }
//        }
//    }

    public List<Map<String, Object>> validateCart(User user) {

        List<CartItem> items = cartItemRepository.findByUserWithProduct(user);

        List<Map<String, Object>> errors = new ArrayList<>();

        if (items.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "El carrito está vacío");
            errors.add(error);
            return errors;
        }

        for (CartItem item : items) {
            Product product = item.getProduct();

            if (product == null) {
                errors.add(Map.of(
                        "productId", null,
                        "message", "Producto no disponible"
                ));
                continue;
            }

            if (product.getStock() <= 0) {
                errors.add(Map.of(
                        "productId", product.getId(),
                        "message", "Sin stock"
                ));
            }

            if (item.getQuantity() > product.getStock()) {
                errors.add(Map.of(
                        "productId", product.getId(),
                        "message", "Stock insuficiente para " + product.getName(),
                        "availableStock", product.getStock()
                ));
            }

            if (item.getQuantity() <= 0) {
                errors.add(Map.of(
                        "productId", product.getId(),
                        "message", "Cantidad inválida"
                ));
            }
        }

        return errors;
    }
}