package com.canarypets.backend.controllers;

import com.canarypets.backend.services.UserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/premium")
public class PremiumController {
    @Autowired
    UserService userService;

    @GetMapping
    public String premium(@NotNull Model model) {
        return "premium/premium";
    }

    @PostMapping
    //@PreAuthorize("isAuthenticated()") // Se podría haber hecho con esto pero esto es otra forma de hacerlo
    public String premium(Authentication authentication, RedirectAttributes redirectAttributes) {

        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Si no está logueado
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            return "redirect:/auth/login"; // Si está logueado, existe userDetails
        }


        userService.addRole(userDetails.getUsername(), "PREMIUM");

        // Actualizar roles en sesión
        List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
        authorities.add(new SimpleGrantedAuthority("ROLE_PREMIUM"));
        /*authorities.forEach(auth -> {
            System.out.println(auth.getAuthority());
        });*/

        Authentication reAuth =
                new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                        authentication.getCredentials(),
                        authorities);
        // Actualizar la autenticación del usuario
        SecurityContextHolder.getContext().setAuthentication(reAuth);

        // Mensaje flash
        redirectAttributes.addFlashAttribute("toastSuccess", "¡Ya eres un usuario premium! 🎉");

        // Volver a la misma página
        return "redirect:/premium";
    }
}
