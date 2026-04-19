package com.canarypets.backend.services;

import com.canarypets.backend.DTOs.OrderRequestDTO;
import com.canarypets.backend.models.*;
import com.canarypets.backend.repositories.OrderRepository;
import com.canarypets.backend.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    // Nota: Si no se usa @RequiredArgsConstructor entonces se tiene que poner @Autowired
    private final CartService cartService;

    @Transactional
    public Order createOrder(OrderRequestDTO dto, User user) {

        cartService.validateCart(user); // Importante validar carrito

        // Crear order
        Order order = new Order();

        // Datos básicos
        order.setUser(user);
        order.setStatus("PENDING");

        // Envío DTO
        order.setEmail(dto.getEmail());
        order.setNombre(dto.getNombre());
        order.setApellidos(dto.getApellidos());
        order.setTelefono(dto.getTelefono());
        order.setDireccionEnvio(dto.getDireccionEnvio());
        order.setDireccionEnvio2(dto.getDireccionEnvio2());
        order.setMunicipio(dto.getMunicipio());
        order.setProvincia(dto.getProvincia());
        order.setCodigoPostal(dto.getCodigoPostal());
        order.setSameAsShipping(dto.isSameAsShipping());

        // Items
        List<OrderItem> items = new ArrayList<>();
        float total = 0f;

        ShoppingCart cart = cartService.getCart(user);

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }

        for (CartItem cartItem : cart.getItems()) {

            Product product = productRepository./*findById*/
            findByIdForUpdate(cartItem.getProduct().getId()) // Comprobar si existe por si acaso
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            //Se usa findByIdForUpdate para evitar un race condition, de forma que
            // se bloquea la fila en DB y nadie más puede comprar ese producto hasta que termine

            int qty = cartItem.getQuantity();

            // Validar stock
            if (qty > product.getStock()) {
                throw new IllegalStateException(
                        "Stock insuficiente para: " + product.getName()
                );
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(qty);

            // Precio real (no frontend) -> precio premium / normal
            float price = user.hasRole("PREMIUM")
                    ? product.getPremiumDiscountPrice().floatValue()
                    : product.getPrice().floatValue();

            item.setPriceAtPurchase(price);

            total += price * qty;

            // Descontar stock
            product.setStock(product.getStock() - qty);

            items.add(item);
        }

        order.setItems(items);

        // Envío
        float shipping = total > 0 ? 3.99f : 0f;
        order.setOrderTotal(BigDecimal.valueOf(total + shipping));

        // Guardar
        order = orderRepository.save(order);

        // Guardar productos actualizados
        productRepository.saveAll(
                items.stream().map(OrderItem::getProduct).toList()
        );

        // Vaciar carrito
        cartService.clearCart(user);

        return order;
    }
}