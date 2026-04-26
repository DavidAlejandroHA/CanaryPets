package com.canarypets.backend.services;

import com.canarypets.backend.DTOs.OrderRequestDTO;
import com.canarypets.backend.models.*;
import com.canarypets.backend.repositories.OrderRepository;
import com.canarypets.backend.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    // Nota: Si no se usa @RequiredArgsConstructor entonces se tiene que poner @Autowired
    private final CartService cartService;

    public Page<Order> getOrdersByUser(User user, Pageable pageable) {
        //return orderRepository.findByUserOrderByCreatedAtDesc(user);
        return orderRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    @Transactional
    public Order createOrder(OrderRequestDTO dto, User user) {

        cartService.validateCart(user); // Importante validar carrito
        //  Validación previa (UX)

        if (dto == null) {
            throw new IllegalArgumentException("Datos del pedido inválidos");
        }

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

        //ShoppingCart cart = cartService.getCart(user);
        List<CartItem> cartItems = cartService.getCartItems(user);

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }

        //for (CartItem cartItem : cart.getItems()) {
        // cart.getItems() es lazy -> Al estar fuera de transacción y no hacer fetch join -> LazyInitializationException
        for (CartItem cartItem : cartItems) { // Manera correcta

            Product product = productRepository./*findByIdForUpdate*/
            findById(cartItem.getProduct().getId()) // Comprobar si existe por si acaso
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            //Se usa findByIdForUpdate para evitar un race condition, de forma que
            // se bloquea la fila en DB y nadie más puede comprar ese producto hasta que termine

            int qty = cartItem.getQuantity();

            // Validar stock
            if (qty > product.getStock()) {
                throw new IllegalStateException(
                        (product.getStock() == 0 ?
                                "Stock agotado para: " :
                                "Stock insuficiente para: ") + product.getName()
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
        float shipping = (total > 0 && !user.hasRole("PREMIUM")) ? 3.99f : 0f;
        order.setOrderTotal(BigDecimal.valueOf(total + shipping));

        // Guardar productos actualizados (útil solo al descontar stock)
        // Realmente no es necesario al estar dentro de @Transactional
        productRepository.saveAll(
                items.stream().map(OrderItem::getProduct).toList()
        );

        // Guardar pedido
        order = orderRepository.save(order);

        // Vaciar carrito
        cartService.clearCart(user);

        return order;
    }

    public Optional<Order> getOrderDetail(Long orderId, User user) {
        return orderRepository.findByIdAndUserWithItems(orderId, user);
    }
}