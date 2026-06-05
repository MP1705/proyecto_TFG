package com.daw.proyecto_v2.RestController;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daw.proyecto_v2.Services.ReservaService;
import com.daw.proyecto_v2.entity.Reserva;

@RestController
@RequestMapping("/api/camarero")
public class CamareroRestController {

    private final ReservaService reservaService;

    public CamareroRestController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    // El camarero pide las reservas en formato JSON
    @GetMapping("/reservas")
    public ResponseEntity<List<Reserva>> listarReservasParaCamarero(
        @RequestParam(required = false) 
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
          
        List<Reserva> reservas = reservaService.obtenerReservas(fecha);
        
        return ResponseEntity.ok(reservas);
    }

    

}