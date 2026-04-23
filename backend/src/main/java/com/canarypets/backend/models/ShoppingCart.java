package com.canarypets.backend.models;

import jakarta.persistence.*;

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
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public ShoppingCart() {}

    /*public void addCartItem(CartItem item) {
        items.add(item);
        item.setCart(this);
    }*/

    /*public void deleteCartItem(CartItem item) {
        items.remove(item);
    }*/

    public float calculateTotal() {
        return (float) items.stream()
                .mapToDouble(cartItem -> cartItem.getTotalPrice().floatValue())
                .sum();
    }

    /*public void emptyCart() {
        items.clear();
    }*/

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    public List<CartItem> getItems() {return items;}
    public void setItems(List<CartItem> items) {this.items = items;}
}
