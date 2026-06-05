package com.daw.proyecto_v2.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReserva;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime hora;

    @Column(nullable = false)
    private LocalTime horaFin;

    @Column(nullable = false)
    private Integer personas;

    @Column(nullable = false)
    private String nombreCliente;

    @Column(nullable = false)
    private String correoCliente;

    @Column(nullable = false)
    private  String telefonoCliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private estadoReserva estado;

    @Column(unique = true, nullable = false)
    private String token = UUID.randomUUID().toString(); 

    @ManyToOne
    @JoinColumn(name="id_mesa", nullable=false)
    private Mesa mesa;

    public Reserva() {}

    public Reserva(LocalDate fecha, LocalTime hora, LocalTime horaFin, Integer personas, String nombreCliente, String correoCliente, estadoReserva estado, Mesa mesa, String telefonoCliente, String token) {
        this.fecha = fecha;
        this.hora = hora;
        this.horaFin = horaFin;
        this.personas = personas;
        this.nombreCliente = nombreCliente;
        this.correoCliente = correoCliente;
        this.estado = estado;
        this.mesa = mesa;
        this.telefonoCliente = telefonoCliente;
        this.token = token;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
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

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Integer getPersonas() {
        return personas;
    }

    public void setPersonas(Integer personas) {
        this.personas = personas;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    public estadoReserva getEstado() {
        return estado;
    }

    public void setEstado(estadoReserva estado) {
        this.estado = estado;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
