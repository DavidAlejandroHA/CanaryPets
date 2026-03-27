package com.canarypets.backend.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PEDIDOS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, updatable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Float orderTotal;

    @Column(nullable = false)
    private String status;

    // Relación con usuario (opcional pero recomendable)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Relación con items del pedido
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    // Generar fecha automáticamente
    @PrePersist
    protected void onCreate() {
        this.date = LocalDate.now();
    }
    // Alternativa:
    //@PastOrPresent(message = "La fecha no puede ser futura")
    //private LocalDateTime date;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "id_producto")
    //private Product product;

    public Order(){}

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public float calculateTotal() {
        float total = 0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void updateTotal() {
        this.orderTotal = calculateTotal();
    }

    public void clearItems() {
        items.clear();
    }

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public LocalDate getDate() {return date;}
    public void setDate(LocalDate date) {this.date = date;}

    public Float getOrderTotal() {return orderTotal;}
    public void setOrderTotal(Float orderTotal) {this.orderTotal = orderTotal;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    public List<OrderItem> getItems() {return items;}
    public void setItems(List<OrderItem> items) {this.items = items;}
}
