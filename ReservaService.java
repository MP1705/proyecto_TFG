package com.daw.proyecto_v2.Services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.proyecto_v2.DTO.crearReservaDTO;
import com.daw.proyecto_v2.entity.Mesa;
import com.daw.proyecto_v2.entity.Reserva;
import com.daw.proyecto_v2.entity.estadoReserva;
import com.daw.proyecto_v2.repositories.MesaRepository;
import com.daw.proyecto_v2.repositories.ReservaRepository;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final MesaRepository mesaRepository;

    public ReservaService(ReservaRepository reservaRepository,
                          MesaRepository mesaRepository) {
        this.reservaRepository = reservaRepository;
        this.mesaRepository = mesaRepository;
    }

   public Reserva crearReserva(crearReservaDTO dto) {

    // Validaciones básicas
    if (dto.getFecha().isBefore(LocalDate.now())) {
        throw new IllegalArgumentException("No se puede reservar en fechas pasadas");
    }

    if (dto.getHora().isBefore(LocalTime.of(12, 0)) || dto.getHora().isAfter(LocalTime.of(23, 0))) {
        throw new IllegalArgumentException("Horario no válido");
    }

    // Calcular duración según personas
    int minutosDuracion;

    if (dto.getPersonas() <= 2) {
        minutosDuracion = 60;
    } else if (dto.getPersonas() <= 4) {
        minutosDuracion = 90;
    } else {
        minutosDuracion = 120;
    }

    // buffer limpieza
    minutosDuracion += 15;

    LocalTime horaInicio = dto.getHora();
    LocalTime horaFin = horaInicio.plusMinutes(minutosDuracion);

    // Buscar mesas con capacidad suficiente
    List<Mesa> mesasSuficientes = mesaRepository.findAll()
            .stream()
            .filter(m -> m.getCapacidad() >= dto.getPersonas())
            .toList();

    if (mesasSuficientes.isEmpty()) {
        throw new IllegalArgumentException("No hay mesas disponibles");
    }

    // Buscar mesa libre SIN SOLAPAMIENTO
    Mesa mesaDisponible = null;

    for (Mesa m : mesasSuficientes) {

        List<Reserva> conflictos = reservaRepository.buscarSolapadas(
                m.getIdMesa(),
                dto.getFecha(),
                horaInicio,
                horaFin
        );

        if (conflictos.isEmpty()) {
            mesaDisponible = m;
            break;
        }
    }

    if (mesaDisponible == null) {
        throw new IllegalArgumentException("No hay mesas disponibles en ese horario");
    }

    // Crear reserva
    Reserva reserva = new Reserva();
    reserva.setFecha(dto.getFecha());
    reserva.setHora(horaInicio);
    reserva.setHoraFin(horaFin); // 👈 CLAVE
    reserva.setPersonas(dto.getPersonas());
    reserva.setNombreCliente(dto.getNombreCliente());
    reserva.setCorreoCliente(dto.getCorreoCliente());
    reserva.setTelefonoCliente(dto.getTelefonoCliente());
    reserva.setEstado(estadoReserva.CONFIRMADA);
    reserva.setMesa(mesaDisponible);

    return reservaRepository.save(reserva);
}

        
    

    // Validar reserva (sin guardar)
    public void validarReserva(crearReservaDTO dto) {
        Mesa mesa = mesaRepository.findById(dto.getIdMesa())
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));

        if (dto.getPersonas() > mesa.getCapacidad()) {
            throw new IllegalArgumentException("La mesa no tiene capacidad suficiente");
        }
        if (dto.getFecha().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede reservar en fechas pasadas");
        }
        if (dto.getHora().isBefore(LocalTime.of(12, 0)) || dto.getHora().isAfter(LocalTime.of(23, 0))) {
            throw new IllegalArgumentException("Horario no válido");
        }

        boolean ocupada = reservaRepository.existsByMesa_IdMesaAndFechaAndHora(
                mesa.getIdMesa(),
                dto.getFecha(),
                dto.getHora()
        );

        if (ocupada) {
            throw new IllegalArgumentException("La mesa ya está reservada en esa fecha y hora");
        }
    }



    // Listar todas las reservas (admin/camarero)
    public List<Reserva> obtenerTodasLasReservas() {
        return reservaRepository.findAll();
    }

    // Confirmar reserva (admin)
    public void confirmarReserva(Integer id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (reserva.getEstado() == estadoReserva.CANCELADA) {
            throw new RuntimeException("No se puede confirmar una reserva cancelada");
        }

        reserva.setEstado(estadoReserva.CONFIRMADA);
        reservaRepository.save(reserva);
    }

    // Cancelar reserva (admin)
    public void cancelarReservaAdmin(Integer id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        reserva.setEstado(estadoReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    //buscar reserva por id
   public Optional<Reserva> buscarPorId(Integer id) {
    return reservaRepository.findById(id);
}

public void eliminarReserva(Integer id) {
    reservaRepository.deleteById(id);
}

public Integer contarComensalesHoy() {
    return reservaRepository.findAll().stream()
            .filter(r -> r.getFecha(). equals(LocalDate.now()) && r.getEstado() == estadoReserva.CONFIRMADA)
            .mapToInt(Reserva::getPersonas) 
            .sum();
}

public List<Reserva> obtenerReservas(LocalDate fecha) {
    return reservaRepository.findByFechaOptional(fecha);
}

public Optional<Reserva> buscarPorToken(String token) {
    return reservaRepository.findByToken(token);
}

}