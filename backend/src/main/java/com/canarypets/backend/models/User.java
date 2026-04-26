package com.canarypets.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @NotBlank(message = "El campo del email no debe de estar vacío")
    @Column(unique = true, nullable = false)
    //@Size(min = 5)
    private String email;

    @NotNull
    @NotBlank(message = "El campo de la contraseña no debe de estar vacío")
    @Column(nullable = false)
    // https://docs.hibernate.org/validator/5.1/reference/en-US/html/chapter-message-interpolation.html#section-interpolation-with-message-expressions
    @Size(min = 5,
            message = "La contraseña debe al menos de tener {min} caracteres de longitud")
    private String password;

    @NotNull
    @NotBlank(message = "El campo del nombre de usuario no debe de estar vacío")
    @Column(unique = true, nullable = false)
    @Size(min = 3,
            message = "El nombre de usuario debe al menos de tener {min} caracteres de longitud")
    private String nickName;

    //@NotNull
    //@NotBlank(message = "El campo de la dirección no debe de estar vacío")
    @Size(max = 100, message = "La dirección es demasiado larga")
    private String address;

    //@NotNull
    //@NotBlank(message = "El campo del código postal no debe de estar vacío")
    @Pattern(regexp = "[0-9]{5}", message = "Código postal inválido")
    //@Positive -> No aplica a Strings
    private String postalCode;

    @NotNull
    @NotBlank(message = "El campo del país no debe de estar vacío")
    @Size(max = 50, message = "El nombre o descripción del país es demasiado largo")
    private String country;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> favorites = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ShoppingCart shoppingCart;

    public User() {}
    public User(String nickName, String password, String email, String address, String postalCode, List<Role> roles, String country) {
        this.nickName = nickName;
        this.password = password;
        this.email = email;
        this.address = address;
        this.postalCode = postalCode;
        this.roles = roles;
        this.country = country;
    }

    public void addFavorite(Product product) {
        favorites.add(product);
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {return address;}
    public void setAddress(String adress) {this.address = adress;}

    public String getPostalCode() {return postalCode;}
    public void setPostalCode(String postalCode) {this.postalCode = postalCode;}

    public String getCountry() {return country;}
    public void setCountry(String country) {this.country = country;}

    public String getRolesAsText() {
        return roles.stream().map(Role::getName).collect(Collectors.joining(" "));
    }
    public List<Role> getRoles() {
        return roles;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    public boolean hasRole(String roleName) {
        return roles.stream()
                .anyMatch(r -> r.getName().equalsIgnoreCase("ROLE_" +roleName));
    }

    public List<Product> getFavorites() {return favorites;}
    public void setFavorites(List<Product> favorites) {this.favorites = favorites;}

    public ShoppingCart getShoppingCart() {return shoppingCart;}
    public void setShoppingCart(ShoppingCart shoppingCart) {this.shoppingCart = shoppingCart;}
}
