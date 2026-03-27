package com.canarypets.backend.specifications;

import com.canarypets.backend.DTOs.ProductFilterDTO;
import com.canarypets.backend.models.Product;
import com.canarypets.backend.models.Tag;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class ProductSpecification { // Para construir la query en tiempo real según los filtros que existan

    public static Specification<Product> filter(ProductFilterDTO filter) {
        return (root, query, cb) -> {
            query.distinct(true); // Para evitar duplicados

            List<Predicate> predicates = new ArrayList<>();

            // Reutilizar método_
            addTagFilter(predicates, root, cb, "tipo_comida", filter.getTipoComida());
            addTagFilter(predicates, root, cb, "marca", filter.getMarca());
            addTagFilter(predicates, root, cb, "edad", filter.getEdad());

            return cb.and(predicates.toArray(new Predicate[0]));
            // Se une todo_: WHERE tipo = pienso AND marca = purina AND edad = cachorro
        };
    }

    private static void addTagFilter(
            List<Predicate> predicates,
            Root<Product> root,
            CriteriaBuilder cb,
            String type,
            String value) {
        // Solo se aplica si viene en la url
        if (value != null) {
            Join<Product, Tag> tags = root.join("tags"); // Hacer JOIN con la tabla de tags

            predicates.add(cb.and(
                    cb.equal(tags.get("type"), type),
                    cb.equal(tags.get("name"), value)
            )); // Equivalente a poner WHERE tag.type = 'tipo_comida' AND tag.name = 'pienso'
        }
    }
}