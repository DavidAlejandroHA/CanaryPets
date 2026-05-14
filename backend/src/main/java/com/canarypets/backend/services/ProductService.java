package com.canarypets.backend.services;

import com.canarypets.backend.DTOs.ProductEditDTO;
import com.canarypets.backend.exceptions.InvalidSlugException;
import com.canarypets.backend.exceptions.ValidationException;
import com.canarypets.backend.models.Category;
import com.canarypets.backend.models.Product;
import com.canarypets.backend.models.Tag;
import com.canarypets.backend.repositories.CategoryRepository;
import com.canarypets.backend.repositories.ProductRepository;
import com.canarypets.backend.repositories.TagRepository;
import com.canarypets.backend.utils.SlugUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    // Productos por subcategoría (caso normal)
    public List<Product> getByCategorySlug(String slug) {
       // Category category = categoryRepository.findBySlug(slug)
       //         .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        Optional<Category> category = categoryRepository.findBySlug(slug);
        if (category.isEmpty()) {
            return new ArrayList<>(); // Lista vacía en vez de error
        }
        return productRepository.findByCategory(category.get());
    }

    // Productos por categoría padre (incluye subcategorías)
    public List<Product> getByParentAndChildSlug(String parentSlug, String childSlug) {
        /*Category category = categoryRepository
                .findBySlugAndParent_Slug(childSlug, parentSlug).orElseThrow(() -> new RuntimeException("Subcategoría no válida"));*/
        Optional<Category> category = categoryRepository.findBySlugAndParent_Slug(childSlug, parentSlug);
        if (category.isEmpty()) {
            return new ArrayList<>(); // Lista vacía en vez de error
        }
        return productRepository.findByCategory(category.get());
    }

    // // Obtener todos los productos de una categoría padre
    public List<Product> getAllByParentSlug(String parentSlug) {
        Optional<Category> parentOpt = categoryRepository.findBySlug(parentSlug);

        if (parentOpt.isEmpty()) {
            return new ArrayList<>(); // Lista vacía en vez de error
        }
        Category parent = parentOpt.get();
        List<Category> subcategories = parent.getSubcategories();

        if (subcategories == null || subcategories.isEmpty()) {
            return new ArrayList<>();
        }

        return productRepository.findByCategoryIn(subcategories);
    }

    public Map<String, List<Tag>> getAvailableFilters(List<Category> categories) {

        List<Tag> tags = tagRepository.findTagsByCategories(categories);

        Map<String, List<Tag>> filters = new HashMap<>();

        for (Tag tag : tags) {
            filters
                    .computeIfAbsent(tag.getType(), k -> new ArrayList<>())
                    .add(tag);
        }

        return filters;
    }

    public Map<String, Map<String, Long>> getFilterCounts(List<Category> categories) {

        List<Object[]> results = tagRepository.countTagsByCategories(categories);

        Map<String, Map<String, Long>> filters = new HashMap<>();

        for (Object[] row : results) {
            String type = (String) row[0];
            String slug = (String) row[1]; // Antes era name, ahora es slug
            Long count = (Long) row[2];

            filters
                    .computeIfAbsent(type, k -> new HashMap<>())
                    .put(slug, count);
        }

        return filters;
    }

    public String generateUniqueSlug(String baseSlug) {
        /*String slug = baseSlug;
        int i = 1;
        while (productRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + i;
            i++;
        }
        return slug;*/
        return SlugUtils.generateUniqueSlug(baseSlug, s -> productRepository.existsBySlug(s));
        // Evitar duplicados (por si acaso)
    }

    // Recoger categorías más profundas (nietos) // descartado
//    private void collectCategories(Category category, List<Category> list) {
//        list.add(category);
//        if (category.getSubcategories() != null) {
//            for (Category sub : category.getSubcategories()) {
//                collectCategories(sub, list);
//            }
//        }
//    }

    @Transactional
    public void updateProduct(Long id, Product form, Long parentId) {
        ValidationException errors = new ValidationException();

        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // --- SLUG ---
        String slug = form.getSlug() != null ? form.getSlug().trim().toLowerCase() : null;
        form.setSlug(slug);

        if (slug == null || slug.isBlank()) {
            errors.addError("slug", "El slug es obligatorio");
        } else {
            if (slug.contains(" ")) {
                errors.addError("slug", "El slug no puede contener espacios");
            }
            if (!slug.matches("^[a-z0-9-_]+$")) { // No permitir espacios y solo caracteres, números o guiones
                errors.addError("slug", "Formato de slug inválido");
            }
            if (!slug.equals(p.getSlug()) && productRepository.existsBySlug(slug)) {
                errors.addError("slug", "Slug duplicado");
            }
        }

        // --- CATEGORY PADRE ---
        if (parentId == null) {
            errors.addError("category", "Selecciona una categoría padre");
        }

        // --- SUBCATEGORÍA ---
        if (form.getCategory() == null || form.getCategory().getId() == null) {
            errors.addError("category", "Selecciona una subcategoría");
        }

        // --- TAGS ---
        /*if (form.getTags() == null || form.getTags().isEmpty()) {
            errors.addError("tags", "Selecciona al menos un tag");
        }*/

        // --- PRECIO ---
        if (form.getPrice() == null || form.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            // Usar siempre compareTo o .compareTo(new BigDecimal("0.01") para comparar con BigDecimal
            errors.addError("price", "El precio debe de ser al menos de 0.01 euros");
        }

        // Si hay errores -> lanzar
        if (errors.hasErrors()) {
            throw errors;
        }

        // --- SETEO ---
        p.setName(form.getName());
        p.setSlug(slug);
        p.setImageUrl(form.getImageUrl());
        p.setDescription(form.getDescription());
        p.setPrice(form.getPrice());
        p.setStock(form.getStock());
        p.setSolidaryProduct(form.isSolidaryProduct());
        p.setDimensions(form.getDimensions());
        p.setProvider(form.getProvider());

        // Spring crea un Category "vacío" con solo id, pero no está gestionado por JPA
        // por lo que se necesita volver a cargarlo desde BD
        Category category = categoryRepository.findById(form.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        p.setCategory(category);
        //p.setCategory(form.getCategory());

        // Lo mismo ocurre con los tags
        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(
                form.getTags().stream().map(Tag::getId).toList()
        ));
        p.setTags(tags);
        //p.setTags(form.getTags());

        productRepository.save(p);
    }

    public Product getBySlug(String slug) {
        return productRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        // En caso de que el campo Tag de Product no usara Lazy, entonces sería necesario
        // una query en específico
    }

    public ProductEditDTO getProductEditData(Long id, Long parentId) {

        Product product = getById(id);

        // Si no viene parentId -> se saca de la categoría actual
        if (parentId == null && product.getCategory() != null) {
            Category parent = product.getCategory().getParent();
            if (parent != null) {
                parentId = parent.getId();
            }
        }

        List<Category> parentCategories = categoryService.findParents();

        List<Category> categories = (parentId != null)
                ? categoryService.findByParentId(parentId)
                : categoryService.findAll();

        // IMPORTANTE: tags filtrados
            /*List<Tag> tags = (parentId != null)
                    ? tagService.findByCategory_Id(parentId)
                    : tagService.findAll();*/ // Sin uso actualmente
        List<Tag> tags = tagService.findAll();

        Set<Long> selectedTagIds = product.getTags()
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());

        return new ProductEditDTO(
                product,
                parentCategories,
                categories,
                tags,
                selectedTagIds,
                parentId
        );
    }

    public List<Product> getRecommendedProducts() {
        return productRepository.findRandomProducts();
    }
}
