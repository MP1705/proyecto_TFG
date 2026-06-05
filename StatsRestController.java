package com.daw.proyecto_v2.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.proyecto_v2.Services.ReservaService;

@RestController
@RequestMapping("/api/stats")
public class StatsRestController {
        private final ReservaService reservaService;

    public StatsRestController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    // Devuelve cuántas personas hay reservadas para HOY
    @GetMapping("/total-comensales-hoy")
    public ResponseEntity<Integer> totalComensalesHoy() {
        // Debes crear esta lógica en tu Service sumando los 'comensales' de las reservas de hoy
        Integer total = reservaService.contarComensalesHoy(); 
        return ResponseEntity.ok(total);
    }
}
