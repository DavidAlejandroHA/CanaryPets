package com.canarypets.backend.enums;

public enum OrderStatus {

    PENDING("Pendiente", "bg-warning text-dark"),
    PROCESSING("Procesando", "bg-info text-dark"),
    SHIPPED("Enviado", "bg-primary"),
    DELIVERED("Entregado", "bg-success"),
    CANCELLED("Cancelado", "bg-danger");

    private final String label;
    private final String cssClass;

    OrderStatus(String label, String cssClass) {
        this.label = label;
        this.cssClass = cssClass;
    }

    public String getLabel() {
        return label;
    }

    public String getCssClass() {
        return cssClass;
    }
}