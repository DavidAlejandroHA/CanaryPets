package com.canarypets.backend.DTOs;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProfileUpdateDTO {

    @NotNull
    @NotBlank(message = "El campo del nombre de usuario no debe de estar vacío")
    @Size(min = 3,
            message = "El nombre de usuario debe al menos de tener {min} caracteres de longitud")
    private String nickname;

    @Size(max = 100, message = "La dirección es demasiado larga")
    private String address;

    @Pattern(regexp = "[0-9]{5}", message = "Código postal inválido")
    private String postalCode;

    @Size(max = 50, message = "El nombre o descripción del país es demasiado largo")
    private String country;

    public String getNickname() {return nickname;}
    public void setNickname(String nickname) {this.nickname = nickname;}

    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}

    public String getPostalCode() {return postalCode;}
    public void setPostalCode(String postalCode) {this.postalCode = postalCode;}

    public String getCountry() {return country;}
    public void setCountry(String country) {this.country = country;}
}