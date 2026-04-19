package com.canarypets.backend.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PEDIDOS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // 📦 DATOS DE ENVÍO
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String direccionEnvio;

    private String direccionEnvio2; // opcional (piso/puerta)

    @Column(nullable = false)
    private String municipio;

    @Column(nullable = false)
    private String provincia;

    @Column(nullable = false)
    private String codigoPostal;

    // 🧾 FACTURACIÓN
    @Column(nullable = false)
    private boolean sameAsShipping; // checkbox

    @Column(nullable = false, updatable = false)
    private LocalDate date;

    @Column(nullable = false)
    private BigDecimal orderTotal;

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

    public BigDecimal calculateTotal() {
        float total = 0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return BigDecimal.valueOf(total);
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

    public BigDecimal getOrderTotal() {return orderTotal;}
    public void setOrderTotal(BigDecimal orderTotal) {this.orderTotal = orderTotal;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    public List<OrderItem> getItems() {return items;}
    public void setItems(List<OrderItem> items) {this.items = items;}

    public String getEmail() {return user.getEmail();}
    public void setEmail(String email) {this.email = email;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getApellidos() {return apellidos;}
    public void setApellidos(String apellidos) {this.apellidos = apellidos;}

    public String getTelefono() {return telefono;}
    public void setTelefono(String telefono) {this.telefono = telefono;}

    public String getDireccionEnvio() {return direccionEnvio;}
    public void setDireccionEnvio(String direccionEnvio) {this.direccionEnvio = direccionEnvio;}

    public String getDireccionEnvio2() {return direccionEnvio2;}
    public void setDireccionEnvio2(String direccionEnvio2) {this.direccionEnvio2 = direccionEnvio2;}

    public String getMunicipio() {return municipio;}
    public void setMunicipio(String municipio) {this.municipio = municipio;}

    public String getProvincia() {return provincia;}
    public void setProvincia(String provincia) {this.provincia = provincia;}

    public String getCodigoPostal() {return codigoPostal;}
    public void setCodigoPostal(String codigoPostal) {this.codigoPostal = codigoPostal;}

    public boolean isSameAsShipping() {return sameAsShipping;}
    public void setSameAsShipping(boolean sameAsShipping) {this.sameAsShipping = sameAsShipping;}
}
