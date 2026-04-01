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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
