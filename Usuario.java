package com.daw.proyecto_v2.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="usuarios")
public class Usuario {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @Column(nullable=false, unique=true)
    private String correo;

    @Column(nullable=false)
    private String nombre;

    @Column(nullable=false)
    private String contrasena;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "token_expiration")
    private LocalDateTime tokenExpiration;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    public Usuario() {}

    public Usuario(String correo, String nombre, String contrasena, Rol rol, String resetToken, LocalDateTime tokenExpiration) {
        this.correo = correo;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.rol = rol;
        this.resetToken = resetToken;
        this.tokenExpiration = tokenExpiration;
    }

    // Getters y setters
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }
    public LocalDateTime getTokenExpiration() { return tokenExpiration; }
    public void setTokenExpiration(LocalDateTime tokenExpiration) { this.tokenExpiration = tokenExpiration; }


}
