package com.canarypets.backend.models;

import com.canarypets.backend.utils.SlugUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "PRODUCTOS")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    //@Column(unique = true, nullable = false)
    private long id;

    @NotNull(message = "Este campo es obligatorio")
    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(min=1, max=70, message = "El nombre ha de tener entre {min} y {max} caracteres")
    @Column(name = "nombre")
    private String name;

    @NotNull(message = "Este campo es obligatorio")
    @NotBlank(message = "Este campo no puede estar vacío")
    @Column(nullable = false, unique = true, length = 150)
    //@Pattern(regexp = "^[a-z0-9-]+$", message = "Slug inválido")
    private String slug; // "nombre a guardar en la base de datos

    private String imageUrl;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    // Para que al pasarlo a mysql el campo lo asigne como tipo longtext, usar columnDefinition = "TEXT" (que es el mismo de la bd)
    private String description;

    //@Min(value = 1, message = "El valor mínimo es de 1 euro")
    //@Max(value = 100, message = "El valor mínimo es de 100 euros")
    @Column(name = "precio_venta")
    @DecimalMin(value = "0.01")
    private BigDecimal price;

    @Column(name = "cantidad_en_stock")
    private int stock;

    @Column(name = "producto_solidario")
    private boolean solidaryProduct;

    @NotNull(message = "Este campo es obligatorio")
    @NotBlank(message = "Este campo no puede estar vacío")
    @Column(name = "dimensiones")
    private String dimensions;

    //@NotNull(message = "Este campo es obligatorio")
    //@NotBlank(message = "Este campo no puede estar vacío")
    @Size(min=0, max=50, message = "El nombre del proveedor ha de tener entre {min} y {max} caracteres")
    @Column(name = "proveedor")
    private String provider;

    /*@Column(name = "precio_proveedor")
    @DecimalMin(value = "0.01")
    private BigDecimal priceProvider;*/

    @JsonIgnore // Evitar posibles problemas de recursividad con Json
    @ManyToMany(mappedBy = "favorites")
    private List<User> usersThatHasProductAsFavorite = new ArrayList<>();

    @JoinColumn(name = "category_id") // Importante usar un @JoinColumn y no un @Column en las relaciones ManyToOne / OneToMany
    @ManyToOne(
            cascade = {/*CascadeType.MERGE,*/CascadeType.REFRESH, CascadeType.PERSIST},
            fetch = FetchType.LAZY // Nota: Mejor usar LAZY por el tema de tener subcategorías
    )// IMPORTANTE: CascadeType no debe de estar en ALL ya que si no
    // la operación de eliminar se va a aplicar en cascada también al padre
    private Category category;

    /* FetchType.LAZY -> No siempre se necesitarán las tags, se escoge la inicialización a LAZY -> mejor rendimiento */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "products_tags",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<Tag> tags = new HashSet<>();

    // Opcional: Dado que no interesa saber en qué carritos está un producto, no es necesario hacer una relación bidireccional (no es obligatorio anotarlo),
    // por lo que se puede prescindir de anotar la relación y automáticamente se detecta como una relación unidireccional (CartItem -> Product)
    //@OneToMany(mappedBy = "product")
    //private List<CartItem> cartItems;

    public Product(){}

    public String getTagValue(String type) {
        return tags.stream()
                .filter(t -> t.getType().equals(type))
                .map(Tag::getName) // Importante: pasar a name, no slug
                .findFirst()
                .orElse("");
    }

    public String generateSlug(String input) {
        /*return Normalizer.normalize(input.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");*/
        return SlugUtils.generateSlug(this.name);
    }

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getSlug() {return slug;}
    public void setSlug(String slug) {this.slug = slug;}

    public String getImageUrl() {return imageUrl;}
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    public String getDimensions() {return dimensions;}
    public void setDimensions(String dimensions) {this.dimensions = dimensions;}

    public String getProvider() {return provider;}
    public void setProvider(String provider) {this.provider = provider;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public int getStock() {return stock;}
    public void setStock(int stock) {this.stock = stock;}

    public boolean getSolidaryProduct() {return solidaryProduct;}
    public void setSolidaryProduct(boolean solidaryProduct) {this.solidaryProduct = solidaryProduct;}

    public BigDecimal getPrice() {return price;}
    public BigDecimal getPremiumDiscountPrice() {
        return this.price.multiply(new BigDecimal("0.9")); // 10% de descuento
    }
    public void setPrice(float price) {this.price = BigDecimal.valueOf(price);}
    public void setPrice(BigDecimal price) {this.price = price;}

    /*public BigDecimal getPriceProvider() {return priceProvider;}
    public void setPriceProvider(BigDecimal priceProvider) {this.priceProvider = priceProvider;}*/

    public Category getCategory() {return category;}
    public void setCategory(Category category) {this.category = category;}

    public Set<Tag> getTags() {return tags;}
    public void setTags(Set<Tag> tags) {this.tags = tags;}
    public void addTag(Tag tag) {
        //if (this.tags == null) this.tags = new HashSet<>(); // Inicializar
        //if (tag.getProducts() == null) tag.setProducts(new HashSet<>()); // Por si algo fallase en la inicialización

        this.tags.add(tag);
        tag.getProducts().add(this); // Importante: sincronizar ambos lados -> relación bidireccional
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getProducts().remove(this);
    }

    public List<User> getUsersThatHasProductAsFavorite() {return usersThatHasProductAsFavorite;}
    public void setUsersThatHasProductAsFavorite(List<User> usersThatHasProductAsFavorite) {this.usersThatHasProductAsFavorite = usersThatHasProductAsFavorite;}
}
