package com.canarypets.backend.controllers;

import com.canarypets.backend.DTOs.OrderRequestDTO;
import com.canarypets.backend.models.Order;
import com.canarypets.backend.models.User;
import com.canarypets.backend.services.CartService;
import com.canarypets.backend.services.OrderService;
import com.canarypets.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/checkout")
@PreAuthorize("isAuthenticated()")
public class CheckoutController {
    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @GetMapping("")
    public String checkout(Model model) {
        model.addAttribute("orderRequest", new OrderRequestDTO());
        return "checkout";
    }

    @PostMapping("")
    public String processCheckout(@ModelAttribute OrderRequestDTO dto,
                                  Authentication auth,
                                  RedirectAttributes redirectAttributes) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        User user = userService.getUserByEmail(auth.getName());

        try {
            Order order = orderService.createOrder(dto, user); // DTO

            // Opcional: pasar ID
            redirectAttributes.addFlashAttribute("orderId", order.getId());

            //return "redirect:/order/success";
            return "redirect:/checkout-completed";

        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar el pedido: " + e.getMessage());
            return "redirect:/cart";
        }
    }
}
