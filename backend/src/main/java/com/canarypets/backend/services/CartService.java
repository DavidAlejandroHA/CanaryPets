package com.canarypets.backend.services;

import com.canarypets.backend.models.CartItem;
import com.canarypets.backend.models.Product;
import com.canarypets.backend.models.ShoppingCart;
import com.canarypets.backend.models.User;
import com.canarypets.backend.repositories.CartItemRepository;
import com.canarypets.backend.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

        /*CartItem item = cartItemRepository
                .findByUserAndProductId(user, productId)
                .orElseThrow();*/
        CartItem item = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow();

        // Comprobaciones:
        // Comprobar si el producto es eliminado o null (extra seguridad)
        if (product == null) {
            throw new IllegalStateException("Producto no disponible");
        }

        // Sin stock
        if (product.getStock() <= 0) {
            throw new IllegalStateException(
                    "El producto '" + product.getName() + "' está sin stock"
            );
        }

        // Cantidad inválida
        if (quantity <= 0) {
            throw new IllegalStateException("Cantidad inválida");
        }

        // Cantidad mayor que stock
        if (quantity > product.getStock()) {
            /*throw new IllegalStateException(
                    "Stock insuficiente para '" + product.getName() +
                            "'. Disponible: " + product.getStock()
            );*/
            item.setQuantity(product.getStock());
            cartItemRepository.save(item);

            throw new IllegalStateException(
                    "Cantidad ajustada al stock disponible (" + product.getStock() + ")"
            );
        }

        // Si está todo_ correcto, se actualiza sin problemas
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

    public BigDecimal getSubtotal(User user) {
        List<CartItem> items = getCartItems(user);

        BigDecimal subtotal = BigDecimal.ZERO;

        for (CartItem item : items) {
            BigDecimal price = item.getProduct().getPrice();
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());

            subtotal = subtotal.add(price.multiply(quantity));
        }

        return subtotal;
    }

    public BigDecimal getSubtotalWithDiscount(User user) {
        List<CartItem> items = getCartItems(user);

        BigDecimal subtotal = BigDecimal.ZERO;

        for (CartItem item : items) {
            BigDecimal price = user.hasRole("PREMIUM")
                    ? item.getProduct().getPremiumDiscountPrice()
                    : item.getProduct().getPrice();

            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());

            subtotal = subtotal.add(price.multiply(quantity));
        }

        return subtotal;
    }

    public BigDecimal getTotal(User user) {
        BigDecimal subtotal = getSubtotal(user);
        return subtotal.add(getShippingCost(user, subtotal));
    }

    public BigDecimal getTotalWithDiscount(User user) {
        BigDecimal subtotal = getSubtotalWithDiscount(user);
        return subtotal.add(getShippingCost(user, subtotal));
    }

    private BigDecimal getShippingCost(User user, BigDecimal subtotal) {
        if (user.hasRole("PREMIUM")) return BigDecimal.ZERO;
        if (subtotal.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(3.99);
    }
}