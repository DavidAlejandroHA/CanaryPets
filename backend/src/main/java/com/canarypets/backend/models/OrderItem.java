package com.canarypets.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "ITEMS_DE_PEDIDOS")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con pedido
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Relación con producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private float priceAtPurchase;

    @Column(nullable = false)
    private float subtotal;

    public OrderItem(){}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Order getOrder() {return order;}
    public void setOrder(Order order) {this.order = order;}

    public Product getProduct() {return product;}
    public void setProduct(Product product) {this.product = product;}

    public int getQuantity() {return quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

    public float getPriceAtPurchase() {return priceAtPurchase;}
    public void setPriceAtPurchase(float priceAtPurchase) {this.priceAtPurchase = priceAtPurchase;}

    public float getSubtotal() {return subtotal;}
    public void setSubtotal(float subtotal) {this.subtotal = subtotal;}
}