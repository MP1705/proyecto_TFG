package com.daw.proyecto_v2.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class crearReservaDTO {

    
    private String nombreCliente;
    private String correoCliente;
    private String telefonoCliente;
    private LocalDate fecha;
    private LocalTime hora;
    private Integer personas;
    private Integer idMesa;

    public crearReservaDTO() {}

    // Getters y setters
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getCorreoCliente() { return correoCliente; }
    public void setCorreoCliente(String correoCliente) { this.correoCliente = correoCliente; }
    public String getTelefonoCliente() { return telefonoCliente; }
    public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }
    public Integer getPersonas() { return personas; }
    public void setPersonas(Integer personas) { this.personas = personas; }
    public Integer getIdMesa() { return idMesa; }
    public void setIdMesa(Integer idMesa) { this.idMesa = idMesa; }

}
