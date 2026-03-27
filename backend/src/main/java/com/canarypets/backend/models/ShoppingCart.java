package com.canarypets.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CARRITOS")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User owner;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public ShoppingCart() {}

    public void addCartItem(CartItem item) {
        items.add(item);
        item.setCart(this);
    }

    public void deleteCartItem(CartItem item) {
        items.remove(item);
    }

    public float calculateTotal() {
        return (float) items.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }

    public void emptyCart() {
        items.clear();
    }

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public User getOwner() {return owner;}
    public void setOwner(User owner) {this.owner = owner;}

    public List<CartItem> getItems() {return items;}
    public void setItems(List<CartItem> items) {this.items = items;}
}
