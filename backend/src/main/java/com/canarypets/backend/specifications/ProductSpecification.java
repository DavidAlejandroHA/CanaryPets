package com.canarypets.backend.specifications;

import com.canarypets.backend.DTOs.ProductFilterDTO;
import com.canarypets.backend.models.Category;
import com.canarypets.backend.models.Product;
import com.canarypets.backend.models.Tag;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class ProductSpecification { // Para construir la query en tiempo real según los filtros que existan

    public static Specification<Product> filter(ProductFilterDTO filter) { // Este no filtra por categoría
        return (root, query, cb) -> {
            query.distinct(true); // Para evitar duplicados

            List<Predicate> predicates = new ArrayList<>();

            // Reutilizar método_
            addTagFilter(predicates, root, cb, "tipo", filter.getTipos());
            addTagFilter(predicates, root, cb, "marca", filter.getMarcas());
            addTagFilter(predicates, root, cb, "edad", filter.getEdades());

            return cb.and(predicates.toArray(new Predicate[0]));
            // Se une todo_, por ejemplo: WHERE tipo = pienso AND marca = purina AND edad = cachorro
        };
    }

    // Útil si alguna vez se quiere solo 1 categoría, pero ya no es necesario si se usan listas siempre
    public static Specification<Product> filter(ProductFilterDTO filter, Category category) {
        return (root, query, cb) -> {
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            // Filtro por categoría
            if (category != null) {
                predicates.add(cb.equal(root.get("category"), category));
            }

            // Tags
            addTagFilter(predicates, root, cb, "tipo", filter.getTipos());
            addTagFilter(predicates, root, cb, "marca", filter.getMarcas());
            addTagFilter(predicates, root, cb, "edad", filter.getEdades());

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Product> filter(ProductFilterDTO filter, List<Category> categories) {
        return (root, query, cb) -> {
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            // Categorías (padre + subcategorías)
            if (categories != null && !categories.isEmpty()) {
                predicates.add(root.get("category").in(categories));
            }

            // Tags
            addTagFilter(predicates, root, cb, "tipo", filter.getTipos());
            addTagFilter(predicates, root, cb, "marca", filter.getMarcas());
            addTagFilter(predicates, root, cb, "edad", filter.getEdades());

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addTagFilter(
            List<Predicate> predicates,
            Root<Product> root,
            CriteriaBuilder cb,
            String type,
            List<String> values) {

        if (values != null && !values.isEmpty()) { // Solo se aplica si viene en la url o hay tags definidos

            Join<Product, Tag> tags = root.join("tags", JoinType.INNER); // Inner join para que funcione con un solo join
                                                                                    // Join por filtro, no se usa uno compartido
            predicates.add(
                    cb.and(
                            cb.equal(tags.get("type"), type),
                            tags.get("slug").in(values) // Aquí es donde se seleccionan múltiples valores
                    ) // Antes sin slug era: tags.get("name")
            );
        }
    }

// Viejo
//    private static void addTagFilter(
//            List<Predicate> predicates,
//            Root<Product> root,
//            CriteriaBuilder cb,
//            String type,
//            String value) {
//        // Solo se aplica si viene en la url
//        if (value != null) {
//            Join<Product, Tag> tags = root.join("tags"); // Hacer JOIN con la tabla de tags
//
//            predicates.add(cb.and(
//                    cb.equal(tags.get("type"), type),
//                    cb.equal(tags.get("name"), value)
//            )); // Equivalente a poner WHERE tag.type = 'tipo_comida' AND tag.name = 'pienso'
//        }
//    }
}