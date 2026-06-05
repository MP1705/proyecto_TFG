package com.daw.proyecto_v2.DTO;

import com.daw.proyecto_v2.entity.Rol;

public class UsuarioDTO {

    private Integer id;
    private String nombre;
    private String correo;
    private Rol rol;
    
    public UsuarioDTO() {
    }

    public UsuarioDTO(Integer id, String nombre, String correo, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

}
