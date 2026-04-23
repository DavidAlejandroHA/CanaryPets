package com.canarypets.backend.controllers;

import com.canarypets.backend.DTOs.OrderRequestDTO;
import com.canarypets.backend.models.CartItem;
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

import java.util.List;

@Controller
@RequestMapping("/checkout")
@PreAuthorize("isAuthenticated()")
public class CheckoutController {
    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    CartService cartService;

    @GetMapping("")
    public String checkout(Model model, Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());

        List<CartItem> cartItems = cartService.getCartItems(user);

        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("orderRequest", new OrderRequestDTO());
        model.addAttribute("cartItems", cartItems);

        // Totales
        model.addAttribute("cartSubtotal", cartService.getSubtotal(user));
        model.addAttribute("cartTotal", cartService.getTotal(user));

        // Si tienes premium:
        model.addAttribute("cartSubtotalWithDiscount", cartService.getSubtotalWithDiscount(user));
        model.addAttribute("cartTotalWithDiscount", cartService.getTotalWithDiscount(user));

        return "cart/checkout";
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
            redirectAttributes.addFlashAttribute("toastSuccess", "¡Compra completada!");

            //return "redirect:/order/success";
            return "redirect:/checkout/checkout-completed";

        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar el pedido."/* + e.getMessage()*/);
            return "redirect:/cart";
        }
    }

    @GetMapping("/checkout-completed")
    public String checkoutCompleted(Model model,
                                    @ModelAttribute("orderId") Long orderId,
                                    @ModelAttribute("toastSuccess") String success) {

        // Si alguien entra manualmente
        if (orderId == null || success == null) {
            return "redirect:/";
        }

        model.addAttribute("orderId", orderId);
        model.addAttribute("toastSuccess", success);

        return "checkout/checkout-completed";
    }

}
