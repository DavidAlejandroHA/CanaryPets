package com.canarypets.backend.controllers;

import com.canarypets.backend.models.User;
import com.canarypets.backend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;

    //https://stackoverflow.com/questions/13261794/display-error-messages-in-spring-login
    @GetMapping("/login")
    public String login(HttpServletRequest request, @NotNull Model model) {
        /*HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = "Error: " + ex.getMessage();
            }
        }*/
        //model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("user", new User());
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(@NotNull Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        //https://stackoverflow.com/questions/65759203/how-to-display-message-from-bindingresult-rejectmessage-in-thymeleaf-page
        // Añadiendo errores customizados en base a comprobaciones externas
        if (userService.getUserByEmail(user.getEmail()) != null) {
            bindingResult.rejectValue("email", "errors.email", "¡Ese email ya está registrado!");
        }
        if (!bindingResult.hasErrors()) {
            userService.saveUser(user);
            return "redirect:/index";
        } else {
            return "auth/register";
        }
    }
}
