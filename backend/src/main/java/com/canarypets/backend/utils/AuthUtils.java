package com.canarypets.backend.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

public class AuthUtils {
    public static boolean isUserLogged(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken)
                && !("anonymousUser".equals(authentication.getName()));
    }
}
