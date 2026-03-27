package com.canarypets.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TAGS")
public class Tag {
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
    private String type; // "edad", "tipo_comida", "marca"

    @ManyToMany(mappedBy = "tags")
    private List<Product> products = new ArrayList<>();

    public Tag(){}

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public List<Product> getProducts() {return products;}
    public void setProducts(List<Product> products) {this.products = products;}
}
