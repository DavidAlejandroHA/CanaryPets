package com.canarypets.backend.DTOs;

import java.util.List;

public class ProductFilterDTO {

    private List<String> marca;
    private List<String> tipo;
    private List<String> edad;

    private String search;

    public List<String> getTipo() {return tipo;}
    public void setTipo(List<String> tipo) {this.tipo = tipo;}

    public List<String> getMarca() {return marca;}
    public void setMarca(List<String> marca) {this.marca = marca;}

    public List<String> getEdad() {return edad;}
    public void setEdad(List<String> edad) {this.edad = edad;}

    public String getSearch() {return search;}
    public void setSearch(String search) {this.search = search;}
}