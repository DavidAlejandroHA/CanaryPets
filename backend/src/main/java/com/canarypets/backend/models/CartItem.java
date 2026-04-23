package com.canarypets.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

    //private float totalPrice; // En OrderItem

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
        //calculateSubtotal();
    }

    // helpers (muy útiles)
    public BigDecimal getTotalPrice() {
        return product.getPrice()
                .multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP); // Redondear a 2 decimales
        //return product.getPrice().floatValue() * quantity; // Devuelve muchos decimales
    }

    /*public void calculateSubtotal() {
        this.subtotal = product.getPrice().floatValue() * quantity;
    }*/

    public BigDecimal getTotalPriceWithDiscount() {
        return product.getPremiumDiscountPrice()
                .multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP); // Redondear a 2 decimales
        //return product.getPremiumDiscountPrice().floatValue() * quantity;
    }

    //public float getSubtotal() {return subtotal;}
    //public void setSubtotal(float subtotal) {this.subtotal = subtotal;}

    public ShoppingCart getCart() {return cart;}
    public void setCart(ShoppingCart cart) {this.cart = cart;}

    public Product getProduct() {return product;}
    public void setProduct(Product product) {this.product = product;}
}
