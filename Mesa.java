package com.daw.proyecto_v2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Mesa {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMesa;

    private Integer capacidad;


    public Mesa() {
    }

    public Mesa(Integer capacidad, Integer idMesa) {
        this.capacidad = capacidad;
        this.idMesa = idMesa;
    }

    public Integer getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(Integer idMesa) {
        this.idMesa = idMesa;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }   

}
