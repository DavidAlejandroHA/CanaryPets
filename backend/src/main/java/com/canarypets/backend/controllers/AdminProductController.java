package com.canarypets.backend.controllers;

import com.canarypets.backend.models.Product;
import com.canarypets.backend.services.CategoryService;
import com.canarypets.backend.services.ProductService;
import com.canarypets.backend.services.TagService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final TagService tagService;

    public AdminProductController(ProductService productService,
                                  CategoryService categoryService,
                                  TagService tagService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.tagService = tagService;
    }

    // Mostrar formulario
    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id,
                              Model model,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getById(id);

            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("tags", tagService.findAll());

            //return "admin/edit-product";

            // Guardar referer
            String referer = request.getHeader("Referer");
            model.addAttribute("redirectTo", referer);

            return "producto/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/";
        }
    }

    // Guardar cambios
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("product") Product form,
                                //HttpServletRequest request,
                                BindingResult result,
                                @RequestParam(required = false) String redirectTo,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        // VALIDACIÓN FRONT (DTO/FORM)
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("tags", tagService.findAll());
            // Mantener redirectTo
            model.addAttribute("redirectTo", redirectTo);
            return "producto/edit";
        }


        try {
            // Para evitar el error de category null:
            if (form.getCategory() == null || form.getCategory().getId() == null) {
                result.rejectValue("category", "error.category", "Selecciona una categoría");
            }
            productService.updateProduct(id, form);
            redirectAttributes.addFlashAttribute("success", "Producto actualizado");
            //return "redirect:/admin/products/edit/" + id;
        } catch (IllegalArgumentException e) {
            result.rejectValue("slug", "error.slug", e.getMessage());

            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("tags", tagService.findAll());
            return "producto/edit";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar producto: " + e.getMessage());
            //return "producto/edit";


        }
        // Redirigir a la página anterior
        //String referer = request.getHeader("Referer");
        //return "redirect:" + (referer != null ? referer : "/");
        return "redirect:" + (redirectTo != null ? redirectTo : "/");
    }
}