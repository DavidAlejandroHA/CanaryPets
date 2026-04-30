package com.canarypets.backend.controllers;

import com.canarypets.backend.DTOs.ProfileUpdateDTO;
import com.canarypets.backend.models.Order;
import com.canarypets.backend.models.Product;
import com.canarypets.backend.models.User;
import com.canarypets.backend.services.OrderService;
import com.canarypets.backend.services.UserService;
import com.canarypets.backend.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
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
        dto.setNickName(user.getNickName());
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

        try { // Hibernate valida en el commit de la transacción, no en el save(),
            // por lo que hay que manejar los posibles errores de validación con TransactionSystemException
            userService.updateProfile(user, dto);
            redirectAttributes.addFlashAttribute("toastSuccess", "Perfil actualizado correctamente");
        } catch (TransactionSystemException ex) {
            Throwable cause = ex.getRootCause();
            if (cause instanceof ConstraintViolationException cve) {
                cve.getConstraintViolations().forEach(v -> {
                    String field = v.getPropertyPath().toString();
                    result.rejectValue(
                            field,
                            "error." + field,
                            v.getMessage()
                    );
                });
                return "profile/edit";
            }

            throw ex; // Si no es validación, que falle normal
        }

        return "redirect:/profile";
    }

    @GetMapping("/orders")
    public String viewOrders( @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
                              Model model,
                              Authentication auth,
                              RedirectAttributes redirectAttributes) {

        //if (auth == null || !auth.isAuthenticated()) {
        /*if (!AuthUtils.isUserLogged(auth)) {
            return "redirect:/auth/login";
        }*/
        // El @PreAuthorize("isAuthenticated()") ya se encarga de comprobar el auth

        try {
            User user = userService.getUserByEmail(auth.getName());
            Page<Order> ordersPage = orderService.getOrdersByUser(user, PageRequest.of(page, size));

            model.addAttribute("ordersPage", ordersPage);
            model.addAttribute("currentPage", page);

            return "profile/orders";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar pedidos");
            return "redirect:/profile";
        }
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id,
                              Model model,
                              Authentication auth,
                              RedirectAttributes redirectAttributes) {

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        try {
            User user = userService.getUserByEmail(auth.getName());
            Optional<Order> orderOpt = orderService.getOrderDetail(id, user);

            if (orderOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Pedido no encontrado");
                return "redirect:/profile/orders";
            }

            model.addAttribute("order", orderOpt.get());
            return "profile/order-detail";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar el pedido");
            return "redirect:/profile/orders";
        }
    }

    @GetMapping("/favorites")
    public String viewFavorites(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size,
                                Model model,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByEmail(auth.getName());
            Page<Product> favoritesPage = userService.getFavoritesPage(user, page, size);

            model.addAttribute("favoritesPage", favoritesPage);
            return "profile/favorites";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar favoritos");
            return "redirect:/profile";
        }
    }

    @PostMapping("/favorites/remove")
    public String removeFavorite(@RequestParam Long productId,
                                 Authentication auth,
                                 RedirectAttributes redirectAttributes) {

        try {
            User user = userService.getUserByEmail(auth.getName());
            userService.removeFavorite(user, productId);

            redirectAttributes.addFlashAttribute("toastSuccess", "Eliminado de favoritos");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar favorito");
        }

        return "redirect:/profile/favorites";
    }

    @PostMapping("/favorites/add-ajax") // Ajax
    @ResponseBody
    public ResponseEntity<?> addFavorite(@RequestParam Long productId,
                                         Authentication auth) {
        try {
            User user = userService.getUserByEmail(auth.getName());
            userService.addFavorite(user, productId);

            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al añadir favorito");
        }
    }

    @PostMapping("/favorites/add")
    public String addFavoriteFallback(@RequestParam Long productId,
                                      Authentication auth,
                                      RedirectAttributes redirectAttributes,
                                      HttpServletRequest request) {

        try {
            User user = userService.getUserByEmail(auth.getName());
            userService.addFavorite(user, productId);

            redirectAttributes.addFlashAttribute("success", "Añadido a favoritos");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al añadir a favoritos");
        }

        // Volver a la página anterior (product-detail normalmente)
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @PostMapping("/favorites/toggle")
    public String toggleFavorite(@RequestParam Long productId,
                                 Authentication auth,
                                 RedirectAttributes redirectAttributes,
                                 HttpServletRequest request) {
        try {
            User user = userService.getUserByEmail(auth.getName());
            userService.toggleFavorite(user, productId);

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar favorito");
        }

        //return "redirect:/profile/favorites";
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/home");
    }

    @PostMapping("/favorites/toggle-ajax")
    @ResponseBody
    public ResponseEntity<?> toggleFavoriteAjax(@RequestParam Long productId,
                                                Authentication auth) {

        try {
            User user = userService.getUserByEmail(auth.getName());
            boolean isNowFavorite = userService.toggleFavorite(user, productId);

            return ResponseEntity.ok().body(Map.of(
                    "favorite", isNowFavorite
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar favorito");
        }
    }
}
