package com.canarypets.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "CATEGORIAS",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"slug", "parent_id"})
                //mismo slug ❌ dentro del mismo padre
                //mismo slug ✔️ en distintos padres
        }
)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //@Id
    @NotNull(message = "Este campo es obligatorio")
    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(min=1, max=50, message = "El nombre ha de tener entre {min} y {max} caracteres")
    //@JoinColumn(name = "name")
    @Column(name = "name", length = 50, unique = true, nullable = false) // Importante que los nombres sean los mismos que en la base de datos
    private String name;

    @NotNull(message = "Este campo es obligatorio")
    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(min=1, max=50, message = "El nombre ha de tener entre 1 y 50 caracteres")
    @Column(name = "descripcion_texto")
    private String description;

    @Column(unique = true, nullable = false)
    private String slug;

    //@Column(name = "imagen")
    //private String image;

    @Column(name = "products_id")
    @OneToMany(
            mappedBy = "category",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            orphanRemoval = false,
            fetch = FetchType.EAGER
    )
    private List<Product> products = new ArrayList<>();

    // Categoría padre
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent; // Null == es categoría padre

    // Subcategorías
    @OneToMany(mappedBy = "parent")
    private List<Category> subcategories = new ArrayList<>();

    public Category(){}

    @PrePersist
    @PreUpdate
    public void prePersist() {
        generateSlug();
    }

    public void generateSlug() {
        this.slug = this.name
                .toLowerCase()
                .replace(" ", "-");
    }

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    //public String getImage() {return image;}
    //public void setImage(String image) {this.image = image;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getSlug() {return slug;}
    public void setSlug(String slug) {this.slug = slug;}

    public List<Product> getProducts() {return products;}
    public void setProducts(List<Product> products) {this.products = products;}

    public Category getParent() {return parent;}
    public void setParent(Category parent) {this.parent = parent;}

    public List<Category> getSubcategories() {return subcategories;}
    public void setSubcategories(List<Category> subcategories) {this.subcategories = subcategories;}
}
