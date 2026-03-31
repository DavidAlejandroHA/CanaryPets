package com.canarypets.backend.utils;

import java.text.Normalizer;
import java.util.function.Function;

public class SlugUtils {
    public static String generateSlug(String input) {
        if (input == null) return null;

        // Normalizar (quitar tildes)
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String withoutAccents = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Limpiar caracteres raros y generar slug (limpiar y formatear)
        return withoutAccents
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "") // quitar símbolos raros/caracteres especiales
                .trim()
                .replaceAll("\\s+", "-") // espacios -> guiones
                .replaceAll("-{2,}", "-") // evitar dobles guiones
                .replaceAll("^-|-$", ""); // quitar guiones al inicio/final
    }

    public static String generateUniqueSlug(String base, Function<String, Boolean> exists) {
        String slug = generateSlug(base);
        String uniqueSlug = slug;
        int i = 1;

        while (exists.apply(uniqueSlug)) {
            uniqueSlug = slug + "-" + i;
            i++;
        }

        return uniqueSlug;
    }
}
