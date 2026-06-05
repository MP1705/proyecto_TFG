package com.daw.proyecto_v2.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

import com.daw.proyecto_v2.entity.estadoReserva;

public class ReservaDTO {

      private Integer id;
    private LocalDate fecha;
    private LocalTime hora;
    private Integer personas;
    private estadoReserva estado;
    private Integer idMesa;


    public ReservaDTO() {
    }


    public ReservaDTO(Integer id, LocalDate fecha, LocalTime hora, Integer personas, estadoReserva estado,
            Integer idMesa) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.personas = personas;
        this.estado = estado;
        this.idMesa = idMesa;
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public LocalDate getFecha() {
        return fecha;
    }


    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }


    public LocalTime getHora() {
        return hora;
    }


    public void setHora(LocalTime hora) {
        this.hora = hora;
    }


    public Integer getPersonas() {
        return personas;
    }


    public void setPersonas(Integer personas) {
        this.personas = personas;
    }


    public estadoReserva getEstado() {
        return estado;
    }


    public void setEstado(estadoReserva estado) {
        this.estado = estado;
    }


    public Integer getIdMesa() {
        return idMesa;
    }


    public void setIdMesa(Integer idMesa) {
        this.idMesa = idMesa;
    }

}
