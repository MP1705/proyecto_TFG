package com.daw.proyecto_v2.DTO;

public class AuthResponse {

     private String token;
     private String rol; 

    // Constructor vacío
    public AuthResponse() {}

    // Constructor con parámetro
    public AuthResponse(String token, String rol) {
        this.token = token;
        this.rol = rol;
    }

    // Getter y Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}

