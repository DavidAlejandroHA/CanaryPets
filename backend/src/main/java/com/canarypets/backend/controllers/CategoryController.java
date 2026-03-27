package com.canarypets.backend.controllers;

import com.canarypets.backend.DTOs.ProductFilterDTO;
import com.canarypets.backend.models.Category;
import com.canarypets.backend.models.Product;
import com.canarypets.backend.repositories.ProductRepository;
import com.canarypets.backend.services.CategoryService;
import com.canarypets.backend.services.ProductService;
import com.canarypets.backend.specifications.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
    @GetMapping("/{parentSlug}")
    public String viewParentCategory(
            @PathVariable String parentSlug,
            Model model) {

        // Obtener categoría padre
        Category parent = categoryService.getBySlug(parentSlug);

        // Subcategorías
        List<Category> subcategories = parent.getSubcategories();

        // Productos de TODAS las subcategorías
        List<Product> products = productService.getAllByParentSlug(parentSlug);

        model.addAttribute("category", parent);
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("products", products);

        return "category/category"; // vista (puede ser products también)
    }

    // Ver subcategoría (ej: /categoria/perros/comida) // Nota: De esto ya se encarga el endpoint que tiene los filtros
    /*@GetMapping("/{parentSlug}/{childSlug}")
    public String viewSubcategory(
            @PathVariable String parentSlug,
            @PathVariable String childSlug,
            Model model) {

        // Validar y obtener subcategoría
        Category category = categoryService
                .getSubcategory(parentSlug, childSlug);

        // Productos de esa subcategoría
        List<Product> products = productService
                .getByParentAndChildSlug(parentSlug, childSlug);

        model.addAttribute("category", category);
        model.addAttribute("products", products);

        return "products";
    }*/

    // Ver subcategoría (ej: /categoria/perros/comida) + Filtros (si los hay)
    @GetMapping("/{parent}/{child}")
    public String filterProducts(
            @PathVariable String parent,
            @PathVariable String child,
            @RequestParam(required = false) String tipoComida,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String edad,
            Model model) {

        ProductFilterDTO filter = new ProductFilterDTO();
        filter.setTipoComida(tipoComida);
        filter.setMarca(marca);
        filter.setEdad(edad);

        Specification<Product> spec = ProductSpecification.filter(filter);

        List<Product> products = productRepository.findAll(spec);

        model.addAttribute("products", products);

        return "products";
    }
}
