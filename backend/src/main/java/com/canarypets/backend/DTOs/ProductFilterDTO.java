package com.canarypets.backend.DTOs;

public class ProductFilterDTO {

    private String tipoComida;
    private String marca;
    private String edad;

    public String getTipoComida() {return tipoComida;}
    public void setTipoComida(String tipoComida) {this.tipoComida = tipoComida;}

    public String getMarca() {return marca;}
    public void setMarca(String marca) {this.marca = marca;}

    public String getEdad() {return edad;}
    public void setEdad(String edad) {this.edad = edad;}
}