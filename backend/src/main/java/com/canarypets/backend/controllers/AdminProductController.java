package com.canarypets.backend.controllers;

import com.canarypets.backend.DTOs.ProductEditDTO;
import com.canarypets.backend.exceptions.ValidationException;
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
                              @RequestParam(required = false) Long parentId,
                              Model model,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) {
        try {
            ProductEditDTO dto = productService.getProductEditData(id, parentId);

            model.addAttribute("product", dto.getProduct());
            model.addAttribute("parentCategories", dto.getParentCategories());
            model.addAttribute("allCategories", dto.getAllCategories());
            model.addAttribute("allTags", dto.getAllTags());
            model.addAttribute("selectedTagIds", dto.getSelectedTagIds());
            model.addAttribute("selectedParentId", dto.getSelectedParentId()); // IMPORTANTE: para mantener selección en el select

            model.addAttribute("redirectTo", request.getHeader("Referer")); // Guardar referer + redirect

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
                                @RequestParam(required = false) Long parentId,
                                @RequestParam(required = false) String redirectTo,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        // VALIDACIÓN FRONT (DTO/FORM)
        if (result.hasErrors()) {
            loadFormData(model, parentId, redirectTo);
            return "producto/edit";
        }

        try {
            productService.updateProduct(id, form, parentId);
            redirectAttributes.addFlashAttribute("success", "Producto actualizado");

            // Redirigir a la página anterior
            //String referer = request.getHeader("Referer");
            return "redirect:" + (redirectTo != null ? redirectTo : "/");
        } catch (ValidationException e) {
            //result.rejectValue("slug", "error.slug", e.getMessage());
            e.getErrors().forEach(error ->
                    result.rejectValue(error.getField(), error.getCode(), error.getMessage())
            );

            loadFormData(model, parentId, redirectTo);
            return "producto/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar producto: " + e.getMessage());
            return "producto/edit";
        }
    }

    private void loadFormData(Model model, Long parentId, String redirectTo) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("tags", tagService.findAll());
        model.addAttribute("redirectTo", redirectTo);
        model.addAttribute("selectedParentId", parentId);
    }
}