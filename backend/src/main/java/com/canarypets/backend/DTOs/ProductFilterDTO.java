package com.canarypets.backend.DTOs;

import java.util.List;

public class ProductFilterDTO {

    private List<String> marcas;
    private List<String> tipos;
    private List<String> edades;

    public List<String> getTipos() {return tipos;}
    public void setTipos(List<String> tipos) {this.tipos = tipos;}

    public List<String> getMarcas() {return marcas;}
    public void setMarcas(List<String> marcas) {this.marcas = marcas;}

    public List<String> getEdades() {return edades;}
    public void setEdades(List<String> edades) {this.edades = edades;}
}