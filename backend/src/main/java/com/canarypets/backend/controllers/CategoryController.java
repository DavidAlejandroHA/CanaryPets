package com.canarypets.backend.controllers;

import com.canarypets.backend.DTOs.ProductFilterDTO;
import com.canarypets.backend.models.Category;
import com.canarypets.backend.models.Product;
import com.canarypets.backend.models.Tag;
import com.canarypets.backend.repositories.ProductRepository;
import com.canarypets.backend.services.CategoryService;
import com.canarypets.backend.services.ProductService;
import com.canarypets.backend.specifications.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/categoria")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

//    @GetMapping("") // Sería válido para búsqueda simple, pero esto no soporta caracteres especiales como "ñ", "ç", etc
//    public String redirectCategory(
//            @RequestParam String parent,
//            @RequestParam(required = false) String search
//    ) {
//        String url = "/categoria/" + parent;
//
//        if (search != null && !search.isBlank()) {
//            url += "?search=" + search;
//        }
//        System.out.println("REDIRECTING TO: " + url);
//        return "redirect:" + url;
//        // Redirigir de /categoria?parent=perros&search=pienso
//        // a -> /categoria/perros?search=pienso
//    }

    // Para la barra de búsqueda
    @PostMapping("") // Es necesario usar POST para enviar datos más complejos y bien codificados
    public String filterRedirect(
            @RequestParam String parent,
            @RequestParam(required = false) String child,
            @RequestParam(required = false) String search,
            ProductFilterDTO filter
    ) {

        StringBuilder url = new StringBuilder("/categoria/" + parent);

        if (child != null && !child.isBlank()) {
            url.append("/").append(child);
        }

        List<String> params = new ArrayList<>();

        // 🔍 búsqueda
        if (search != null && !search.isBlank()) {
            params.add("search=" + URLEncoder.encode(search, StandardCharsets.UTF_8));
        }

        // 🏷️ filtros (IMPORTANTE)
        if (filter.getMarca() != null) {
            filter.getMarca().forEach(m -> params.add("marcas=" + m));
        }

        if (filter.getTipo() != null) {
            filter.getTipo().forEach(t -> params.add("tipos=" + t));
        }

        if (filter.getEdad() != null) {
            filter.getEdad().forEach(e -> params.add("edades=" + e));
        }
        // ejemplo precio
//        if (filter.getMinPrice() != null) {
//            params.add("minPrice=" + filter.getMinPrice());
//        }
//        if (filter.getMaxPrice() != null) {
//            params.add("maxPrice=" + filter.getMaxPrice());
//        }
        if (!params.isEmpty()) {
            url.append("?").append(String.join("&", params));
        }

        return "redirect:" + url.toString();
    }

    // Ver categoría padre (ej: /categoria/perros)
    @GetMapping("/{parent}") // Importante: No usar {parentSlug}, ya que este ya se usa como dato en el modelo
    // (se mezcla con "parentSlug" y spring no enlaza bien, lo que hace que se rompa el flujo y lleve hacia el login)
    public String viewParentCategory(
            @PathVariable String parent,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) List<String> tipo,
            ProductFilterDTO filter,
            Pageable pageable,
            Model model) {

        Category category = categoryService.getBySlug(parent); // <- Categorías padre
        //categoryService.findBySlugAndParentIsNull(parentSlug);
        model.addAttribute("parentSlug", parent);
        model.addAttribute("currentCategory", parent);
        //model.addAttribute("allCategories", categoryService.getParentCategories()); // Definido en el controller GlobalModelAttributtes
        return buildCategoryView(category, filter, pageable, model, search, tipo);
    }

    // Ver subcategoría (ej: /categoria/perros/comida) + Filtros (si los hay)
    @GetMapping("/{parent}/{child}")
    public String viewSubCategory(
            @PathVariable String parent,
            @PathVariable String child,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) List<String> tipo,
            ProductFilterDTO filter,
            Pageable pageable,
            Model model) {

        //Category category = categoryService.getBySlug(child);
        Category category = categoryService.getBySlugAndParent_Slug(parent, child);
        model.addAttribute("parentSlug", parent);
        model.addAttribute("childSlug", child);
        model.addAttribute("currentCategory", parent);
        return buildCategoryView(category, filter, pageable, model, search, tipo);
    }

    private String buildCategoryView(Category category, ProductFilterDTO filter, Pageable pageable, Model model,
                                     String search, List<String> tipos) {

        List<Category> categories =
                categoryService.getCategoryWithSubcategories(category);

        Specification<Product> spec =
                ProductSpecification.filter(filter, categories);

        // Al final no se va a incluir para no añadir mayor complejidad / no es necesario del todo_
        //Sort sort = Sort.by("price").ascending(); // Ordenar por precio

        /*if ("price_desc".equals(filter.getSort())) {
            sort = Sort.by("price").descending();
        }*/

        PageRequest pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize()
                //, sort
        );

        Page<Product> page =
                //productRepository.findAll(spec, pageable); // Con Page
                productRepository.findAll(spec, pageRequest); // Con PageRequest

        //List<Product> products = productRepository.findAll(spec); // Sin Page

        // Importante: filtros también con subcategorías - Obtener los filtros disponibles que hay en cada categoría
        Map<String, List<Tag>> filters =
                productService.getAvailableFilters(categories);

        Map<String, Map<String, Long>> filterCounts = // Contador de filtros
                productService.getFilterCounts(categories);

        filter.setSearch(search);
        filter.setTipo(tipos);
        model.addAttribute("search", search); // Añadir búsqueda
        model.addAttribute("products", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("filters", filters);
        model.addAttribute("filterCounts", filterCounts);
        model.addAttribute("selectedFilters", filter);
        //model.addAttribute("parent", category); // objeto Category
        // /* Se podría hacer parent.slug en las vistas para obtener el slug del parent, pero es más siempre utilizar
        // el String "parent" de los métodos del controller */
        return "category/category";
    }
}
