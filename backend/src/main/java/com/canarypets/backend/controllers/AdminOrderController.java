package com.canarypets.backend.controllers;

import com.canarypets.backend.DTOs.OrderFilterFormDTO;
import com.canarypets.backend.enums.OrderStatus;
import com.canarypets.backend.models.Order;
import com.canarypets.backend.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
@RequestMapping("/admin/orders")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String listOrders(
            @ModelAttribute("filter") OrderFilterFormDTO form,
            BindingResult result,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            /*@RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,*/
            RedirectAttributes ra,
            HttpServletRequest request,
            Model model
    ) {
        // Almacenar url en cada botón de ver detalle, para permitir poder volver hacia atrás
        String query = request.getQueryString();
        String currentUrl = request.getRequestURI() +
                (query != null ? "?" + query : "");

        model.addAttribute("currentUrl", currentUrl);
        model.addAttribute("status", form.getStatus());
        model.addAttribute("search", form.getSearch());
        model.addAttribute("dateFrom", form.getDateFrom());
        model.addAttribute("dateTo", form.getDateTo());

        try {
            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by("createdAt").descending()
            );

            /*if (search != null && !search.isBlank()) {
                // PRIORIDAD: búsqueda
                ordersPage = orderService.searchOrders(search, pageable);

            } else if (status != null && !status.isEmpty()) {
                // Filtro por estado
                ordersPage = orderService.findByStatus(status, pageable);

            } else {
                // Todos
                ordersPage = orderService.getAllOrders(pageable);
            }*/

            // Validación fechas
            if (form.getDateFrom() != null && form.getDateTo() != null &&
                    form.getDateFrom().isAfter(form.getDateTo())) {

                result.rejectValue("dateFrom", "error.dateFrom",
                        "La fecha 'Desde' no puede ser mayor que 'Hasta'");
            }

            // Validar status
            if (form.getStatus() != null && !form.getStatus().isBlank()) {
                try {
                    OrderStatus.valueOf(form.getStatus());
                } catch (IllegalArgumentException e) {
                    result.rejectValue("status", "error.status",
                            "Estado inválido");
                }
            }

            if (result.hasErrors()) {
                model.addAttribute("ordersPage", Page.empty());
                return "profile/admin-orders";
            }
            Page<Order> ordersPage =
                    orderService.searchByFilters(form.getStatus(), form.getSearch(),
                            form.getDateFrom(), form.getDateTo(), pageable);

            model.addAttribute("ordersPage", ordersPage);
            model.addAttribute("currentPage", page);
            return "profile/admin-orders";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/profile/admin-orders";
        }
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Long id,
                              @RequestParam(required = false) String redirectTo,
                              Model model,
                              HttpServletRequest request,
                              RedirectAttributes ra) {
        try {
            Order order = orderService.getOrderById(id)
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

            // Prioridad: param > referer
            if (redirectTo == null || redirectTo.isBlank()) {
                redirectTo = request.getHeader("Referer");
            }

            model.addAttribute("order", order);
            model.addAttribute("redirectTo", redirectTo);

            return "profile/admin-order-detail";

        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/profile/admin-orders";
        }
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               RedirectAttributes redirectAttributes,
                               HttpServletRequest request) {
        OrderStatus orderStatus; // Spring también puede convertir automáticamente el String en Enum
        try {
            orderStatus = OrderStatus.valueOf(status);
            boolean isValid = Arrays.stream(OrderStatus.values())
                    .map(Enum::name)
                    .anyMatch(s -> s.equals(status));
            if (!isValid) {
                throw new IllegalArgumentException("Error - Estado inválido: " + status);
            }

            orderService.updateStatus(id, orderStatus);
            redirectAttributes.addFlashAttribute("success", "Estado actualizado correctamente");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error actualizando estado");
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/profile/admin-orders");
    }

    // Sin uso actualmente
    @PostMapping("/{id}/status-ajax")
    @ResponseBody
    public ResponseEntity<?> updateStatusAjax(
            @PathVariable Long id,
            @RequestParam String status) {

        try {
            OrderStatus newStatus = OrderStatus.valueOf(status);
            orderService.updateStatus(id, newStatus);

            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Estado inválido");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error actualizando estado");
        }
    }
}