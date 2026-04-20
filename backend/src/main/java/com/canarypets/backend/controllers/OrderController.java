package com.canarypets.backend.controllers;

import com.canarypets.backend.DTOs.OrderRequestDTO;
import com.canarypets.backend.models.Order;
import com.canarypets.backend.models.User;
import com.canarypets.backend.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/*@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor*/
public class OrderController {

    //private final OrderService orderService;

    // A futuro
    /*@PostMapping("/create") // Ajax
    public ResponseEntity<?> createOrder(
            @RequestBody OrderRequestDTO dto,
            Authentication auth
    ) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = (User) auth.getPrincipal();

        try {
            Order order = orderService.createOrder(dto, user);
            return ResponseEntity.ok(order.getId());

        } catch (IllegalStateException e) {
            // Errores de negocio (stock, carrito vacío, etc.)
            return ResponseEntity.badRequest().body( Map.of("error", e.getMessage()));

        } catch (Exception e) {
            // Error inesperado (logs importantes aquí)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el pedido");
        }
    }*/
}