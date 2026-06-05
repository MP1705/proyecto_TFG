package com.daw.proyecto_v2.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.proyecto_v2.DTO.crearReservaDTO;
import com.daw.proyecto_v2.Services.EmailService;
import com.daw.proyecto_v2.Services.ReservaService;
import com.daw.proyecto_v2.entity.Reserva;

@RestController
@RequestMapping("/api/public")
public class ReservaPublicaRest {

  private final ReservaService reservaService;
  private final EmailService emailService;

    public ReservaPublicaRest(ReservaService reservaService, EmailService emailService) {
        this.reservaService = reservaService;
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<?> guardarReserva(@RequestBody crearReservaDTO dto) {
        try {
            // 1. Guardar en BD
            Reserva reserva = reservaService.crearReserva(dto);
            
            // 2. Intentar enviar email (en segundo plano)
            try {
                emailService.enviarConfirmacion(
                    dto.getCorreoCliente(), 
                    dto.getNombreCliente(), 
                    dto.getFecha().toString()
                );
            } catch (Exception e) {
                System.err.println("Error al enviar email: " + e.getMessage());
                // No lanzamos error para que la reserva se considere válida aunque falle el mail
            }

            // 3. CREAR RESPUESTA ENRIQUECIDA 
        // Usamos un Map para añadir campos que NO están en la entidad Reserva
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("reserva", reserva); // Metemos la reserva completa
        respuesta.put("id", reserva.getIdReserva()); // Acceso directo al ID
        respuesta.put("codigoSeguridad", "CONF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        respuesta.put("mensajePersonalizado", "¡Hola " + reserva.getNombreCliente() + "! Tu mesa está casi lista.");
        respuesta.put("timestamp", System.currentTimeMillis());

            // 4. Devolver la reserva creada con estado 201
            return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
            
        } catch (IllegalArgumentException e) {
            // Devolver error 400 si los datos son inválidos
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error esn el servidor");
        }
    }

     @GetMapping("/{token}")
    public ResponseEntity<?> obtenerDetallesReserva(@PathVariable String token) {
        // Buscamos la reserva en la base de datos por su token
        return reservaService.buscarPorToken(token)
                .map(reserva -> ResponseEntity.ok(reserva)) // Si la encuentra, devuelve JSON
                .orElse(ResponseEntity.notFound().build()); // Si no, error 404
    }

}