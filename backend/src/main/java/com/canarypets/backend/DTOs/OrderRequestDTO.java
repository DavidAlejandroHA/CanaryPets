package com.canarypets.backend.DTOs;


public class OrderRequestDTO {

    // Envío
    private String email;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String direccionEnvio;
    private String direccionEnvio2;
    private String municipio;
    private String provincia;
    private String codigoPostal;
    private boolean sameAsShipping;

    // Pago
    private String paymentMethod; // CARD / PAYPAL / TRANSFER

    // Items
    //private List<CartItemDTO> items;

    public String getEmail() {return email;}
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

    public String getPaymentMethod() {return paymentMethod;}
    public void setPaymentMethod(String paymentMethod) {this.paymentMethod = paymentMethod;}

    //public List<CartItemDTO> getItems() {return items;}
    //public void setItems(List<CartItemDTO> items) {this.items = items;}
}