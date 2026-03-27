package com.canarypets.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "ITEMS_DE_CARRITO")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    //@Column(unique = true, nullable = false)
    private long id;

    @Min(1)
    private int quantity;

    private float subtotal;

    @ManyToOne(fetch = FetchType.LAZY) /* Solo se carga lo necesario, mejor rendimiento */
    @JoinColumn(name = "id_carrito")
    private ShoppingCart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    private Product product;

    public CartItem(){}

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public int getQuantity() {return quantity;}
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }

    public void calculateSubtotal() {
        this.subtotal = product.getPrice().floatValue() * quantity;
    }

    public float getSubtotal() {return subtotal;}
    public void setSubtotal(float subtotal) {this.subtotal = subtotal;}

    public ShoppingCart getCart() {return cart;}
    public void setCart(ShoppingCart cart) {this.cart = cart;}

    public Product getProduct() {return product;}
    public void setProduct(Product product) {this.product = product;}
}
