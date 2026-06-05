package com.daw.proyecto_v2.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daw.proyecto_v2.entity.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    Optional<Reserva> findByToken(String token);

    boolean existsByMesa_IdMesaAndFechaAndHora(Integer idMesa, LocalDate fecha, LocalTime hora);

    List<Reserva> findByFecha(LocalDate fecha);

    @Query("SELECT r FROM Reserva r WHERE (:fecha IS NULL OR r.fecha = :fecha)")
    List<Reserva> findByFechaOptional(@Param("fecha") LocalDate fecha);

    
    @Query("""
    SELECT r FROM Reserva r
    WHERE r.mesa.idMesa = :mesaId
    AND r.fecha = :fecha
    AND r.hora < :horaFin
    AND r.horaFin > :horaInicio
    """)
    List<Reserva> buscarSolapadas(
        @Param("mesaId") Integer mesaId,
        @Param("fecha") LocalDate fecha,
        @Param("horaInicio") LocalTime horaInicio,
        @Param("horaFin") LocalTime horaFin
    );
}