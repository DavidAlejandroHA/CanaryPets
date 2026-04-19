package com.canarypets.backend.controllers;

import com.canarypets.backend.models.CartItem;
import com.canarypets.backend.models.ShoppingCart;
import com.canarypets.backend.models.User;
import com.canarypets.backend.services.CartService;
import com.canarypets.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
@PreAuthorize("isAuthenticated()")
public class CartController {
    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;

    // Ver carrito
    @GetMapping
    public String viewCart(Model model, Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        ShoppingCart cart = cartService.getCart(user);

        model.addAttribute("cartItems", cart.getItems());

        return "cart/cart";
    }

    // Añadir
    @PostMapping("/add")
    public String addToCart(
            @RequestParam Long productId,
            @RequestParam int quantity,
            Authentication auth
    ) {
        if (!isUserLogged(auth)) {
            return "redirect:/auth/login";
        }
        //System.out.println("ENTRA EN ADD NORMAL");
        // Añadir producto al carrito
        addProductToCart(auth, productId, quantity);
        //User user = userService.getUserByEmail(auth.getName());
        //cartService.addProduct(user, productId, quantity);

        return "redirect:/cart"; // o volver atrás
    }

    // Añadir AJAX (sin recargar la página)
    @PostMapping("/add-ajax")
    @ResponseBody
    public ResponseEntity<?> addAjax(@RequestBody Map<String, Object> body,
                                     Authentication auth) {
        //if (auth == null || !auth.isAuthenticated()) {
        if (!isUserLogged(auth)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long productId = Long.valueOf(body.get("productId").toString());
        int quantity = Integer.parseInt(body.get("quantity").toString());

        addProductToCart(auth, productId, quantity);

        return ResponseEntity.ok().build();
    }

    // Eliminar (JS + fallback)
    @PostMapping("/remove")
    public String remove(@RequestParam Long productId,
                         Authentication auth) {

        User user = userService.getUserByEmail(auth.getName());
        cartService.removeProduct(user, productId);

        return "redirect:/cart";
    }

    // Eliminar AJAX
    @PostMapping("/remove/{productId}")
    @ResponseBody
    public ResponseEntity<?> removeAjax(@PathVariable Long productId, Authentication auth) {
        if (!isUserLogged(auth)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userService.getUserByEmail(auth.getName());
        cartService.removeProduct(user, productId);

        return ResponseEntity.ok().build();
    }

    // Update fallback
    @PostMapping("/update")
    public String updateFallback(@RequestParam Long productId,
                                 @RequestParam int quantity,
                                 Authentication auth) {

        User user = userService.getUserByEmail(auth.getName());
        cartService.updateQuantity(user, productId, quantity);

        return "redirect:/cart";
    }

    // Update AJAX
    @PostMapping("/update-ajax")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody Map<String, Object> body,
                       Authentication auth) {
        if (!isUserLogged(auth)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long productId = Long.valueOf(body.get("id").toString());
        int quantity = Integer.parseInt(body.get("quantity").toString());

        User user = userService.getUserByEmail(auth.getName());
        cartService.updateQuantity(user, productId, quantity);

        return ResponseEntity.ok().build();
    }

    // Validar carrito - Ajax
    @PostMapping("/validate-ajax")
    public ResponseEntity<?> validateCart_Ajax(Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userService.getUserByEmail(auth.getName());

        List<Map<String, Object>> errors = cartService.validateCart(user);

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        return ResponseEntity.ok().build();
    }

    // Validar sin js (fallback)
    @PostMapping("/validate")
    public String validateCartFallback(Authentication auth, RedirectAttributes redirectAttributes) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        User user = userService.getUserByEmail(auth.getName());

        List<Map<String, Object>> errors = cartService.validateCart(user);

        if (!errors.isEmpty()) {
            // solo mostramos el primer error como fallback simple
            redirectAttributes.addFlashAttribute("error", errors.get(0).get("message"));
            return "redirect:/cart";
        }

        return "redirect:/checkout";
    }

    private void addProductToCart(Authentication auth, Long productId, int quantity) {
        if ("anonymousUser".equals(auth.getName())) {
            throw new RuntimeException("Usuario no autenticado");
        }

        User user = userService.getUserByEmail(auth.getName());
        cartService.addProduct(user, productId, quantity);
    }

    private boolean isUserLogged(Authentication authentication) {
        return authentication != null &&
                !(authentication instanceof AnonymousAuthenticationToken)
                && !("anonymousUser".equals(authentication.getName()));
    }
}
