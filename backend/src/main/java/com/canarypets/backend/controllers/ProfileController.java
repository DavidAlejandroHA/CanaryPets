package com.canarypets.backend.controllers;

import com.canarypets.backend.DTOs.ProfileUpdateDTO;
import com.canarypets.backend.models.Order;
import com.canarypets.backend.models.User;
import com.canarypets.backend.services.OrderService;
import com.canarypets.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
@PreAuthorize("isAuthenticated()")
public class ProfileController {
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;

    // Ver perfil
    @GetMapping
    public String viewProfile(Model model, Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);
        return "profile/profile";
    }

    // Form editar
    @GetMapping("/edit")
    public String editProfile(Model model, Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());

        ProfileUpdateDTO dto = new ProfileUpdateDTO();
        dto.setNickname(user.getNickName());
        dto.setAddress(user.getAddress());
        dto.setPostalCode(user.getPostalCode());
        dto.setCountry(user.getCountry());

        model.addAttribute("profileForm", dto);

        return "profile/edit";
    }

    // Procesar edición
    @PostMapping("/edit")
    public String updateProfile(
            @Valid @ModelAttribute("profileForm") ProfileUpdateDTO dto,
            BindingResult result,
            Authentication auth,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "profile/edit";
        }

        User user = userService.getUserByEmail(auth.getName());

        userService.updateProfile(user, dto);

        redirectAttributes.addFlashAttribute("toastSuccess", "Perfil actualizado correctamente");

        return "redirect:/profile";
    }

    @GetMapping("/orders")
    public String viewOrders( @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
                              Model model,
                              Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        User user = userService.getUserByEmail(auth.getName());

        //List<Order> orders = orderService.getOrdersByUser(user);
        Page<Order> ordersPage =
                orderService.getOrdersByUser(user, PageRequest.of(page, size));

        //model.addAttribute("orders", orders);
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("currentPage", page);

        return "profile/orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id,
                              Model model,
                              Authentication auth,
                              RedirectAttributes redirectAttributes) {

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        User user = userService.getUserByEmail(auth.getName());

        Optional<Order> orderOpt = orderService.getOrderDetail(id, user);

        if (orderOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Pedido no encontrado");
            return "redirect:/profile/orders";
        }

        model.addAttribute("order", orderOpt.get());

        return "profile/order-detail";
    }
}
