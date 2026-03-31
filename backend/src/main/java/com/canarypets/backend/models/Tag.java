package com.canarypets.backend.models;

import com.canarypets.backend.utils.SlugUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "TAGS",
        uniqueConstraints = @UniqueConstraint(columnNames = {"type", "slug"}) // Evitar duplicados de type y name conjuntos
)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //@Id
    @NotNull(message = "Este campo es obligatorio")
    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(min=1, max=50, message = "El nombre ha de tener entre {min} y {max} caracteres")
    @Column(nullable = false)
    private String name; // "cachorro", "pienso", "purina"

    @NotNull(message = "Este campo es obligatorio")
    @NotBlank(message = "Este campo no puede estar vacío")
    @Column(nullable = false)
    private String type; // "edad", "tipo_comida", "marca"

    @NotNull(message = "Este campo es obligatorio")
    @NotBlank(message = "Este campo no puede estar vacío")
    @Column(nullable = false)
    private String slug; // "nombre a guardar en la base de datos / para los filtros

    @ManyToMany(mappedBy = "tags")
    private Set<Product> products = new HashSet<>();

    public Tag(){}

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public String getSlug() {return slug;}
    public void setSlug(String slug) {this.slug = slug;}

    public void generateSlug() {
        /*this.slug = name
                .toLowerCase()
                .replace(" ", "-")
                .replace("’", "")
                .replace("'", "");*/

        this.slug = SlugUtils.generateSlug(this.name);
    }

    public void generateSlug(String input) {
        this.slug = SlugUtils.generateSlug(input);
    }

    public Set<Product> getProducts() {return products;}
    public void setProducts(Set<Product> products) {this.products = products;}
}
